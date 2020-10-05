package org.jax.mgi.mgd.api.model.bib.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.exception.NonFatalAPIException;
import org.jax.mgi.mgd.api.model.BaseRepository;
import org.jax.mgi.mgd.api.model.acc.dao.AccessionDAO;
import org.jax.mgi.mgd.api.model.acc.dao.LogicalDBDAO;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowTag;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;
import org.jax.mgi.mgd.api.model.bib.translator.LTReferenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.MapMaker;
import org.jax.mgi.mgd.api.util.SearchResults;

/* Is: a repository that deals with ReferenceDomain objects and handles their translation to
 *    Reference entity objects for storage to and retrieval from the database
 * Has: one or more DAOs to facilitate storage/retrieval of the entities from which the
 *    ReferenceDomain object has its data drawn
 * Does: (from the outside, this appears to) retrieve domain objects, store them, search for them
 */
public class LTReferenceRepository extends BaseRepository<LTReferenceDomain> {

	/***--- instance variables ---***/

	@Inject
	private LTReferenceDAO referenceDAO;

	@Inject
	private TermDAO termDAO;
	
	@Inject
	private AccessionDAO accessionDAO;

	@Inject
	private LogicalDBDAO logicaldbDAO;
	
	@Inject
	private MGITypeDAO mgiTypeDAO;

	@Inject
	private MGIReferenceAssocService referenceAssocService;	
	
	LTReferenceTranslator translator = new LTReferenceTranslator();

	/* These work together to allow for a maximum delay of two seconds for retries: */
	private static int maxRetries = 10;		// maximum number of retries for non-fatal exceptions on update operations
	private static int retryDelay = 200;	// number of ms to wait before retrying update operation after non-fatal exception

	/***--- (public) instance methods ---***/

	/* gets a ReferenceDomain object that is fully fleshed out from a Reference
	 */
	@Override
	public LTReferenceDomain get(String key) throws FatalAPIException, APIException {
		LTReference ref = getReference(key);
		LTReferenceDomain domain = translator.translate(ref);
		domain.setStatusHistory(getStatusHistory(domain));
		return domain;	
	}

	@Override
	public SearchResults<LTReferenceDomain> search(Map<String,Object> params) {
		SearchResults<LTReference> refs = referenceDAO.search(params);
		SearchResults<LTReferenceDomain> domains = new SearchResults<LTReferenceDomain>();

		domains.elapsed_ms = refs.elapsed_ms;
		domains.error = refs.error;
		domains.message = refs.message;
		domains.status_code = refs.status_code;
		domains.total_count = refs.total_count;
		domains.all_match_count = refs.all_match_count;

		if (refs.items != null) {
			// walking the references to do the translations individually, because I want a List,
			// not an Iterable

			domains.items = new ArrayList<LTReferenceDomain>();
			for (LTReference ref : refs.items) {
				domains.items.add(translator.translate(ref));
			}
		}
		return domains;
	}

	@Override
	public LTReferenceDomain update(LTReferenceDomain domain, User user) throws FatalAPIException, NonFatalAPIException, APIException {
		LTReference entity = getReference(domain.refsKey);
		log.info("found LTReference/entity");
		applyDomainChanges(entity, domain, user);
		referenceDAO.persist(entity);
		referenceDAO.updateCitationCache(domain.refsKey);		
		return translator.translate(entity);
	}

	/* get a list of events in the status history of the reference with the specified key
	 */
	public List<LTReferenceWorkflowStatusDomain> getStatusHistory(LTReferenceDomain domain) throws APIException {
		List<LTReferenceWorkflowStatusDomain> history = new ArrayList<LTReferenceWorkflowStatusDomain>();
		for (LTReferenceWorkflowStatus event : referenceDAO.getStatusHistory(domain.refsKey)) {
			history.add(new LTReferenceWorkflowStatusDomain(event));
		}
		return history;
	}

	/* set the given workflow_tag for all references identified in the list of keys
	 */
	public void updateInBulk(List<String> refsKey2, String workflow_tag, String workflow_tag_operation, User currentUser) throws FatalAPIException, APIException {
		// if no references or no tags, just bail out as a no-op
		if ((refsKey2 == null) || (refsKey2.size() == 0) || (workflow_tag == null) || (workflow_tag.length() == 0)) {
			return; 
		}

		// if no workflow tag operation is specified, default to 'add'
		if ((workflow_tag_operation == null) || workflow_tag_operation.equals("")) {
			workflow_tag_operation = Constants.OP_ADD_WORKFLOW;
		} else if (!workflow_tag_operation.equals(Constants.OP_ADD_WORKFLOW) && !workflow_tag_operation.equals(Constants.OP_REMOVE_WORKFLOW)) {
			throw new FatalAPIException("Invalid workflow_tag_operation: " + workflow_tag_operation);
		}

		/* for each reference:
		 * 1. if the operation fails in a fatal manner, rethrow that exception immediately.
		 * 2. if the operation fails in a non-fatal manner, wait briefly and try again up to maxRetries times.
		 */
		for (String refsKey : refsKey2) {
			LTReference reference = referenceDAO.getReference(refsKey);
			if (reference != null) {
				int retries = 0;
				boolean succeeded = false;

				while (!succeeded) {
					try {
						if (workflow_tag_operation.equals(Constants.OP_ADD_WORKFLOW)) {
							addTag(reference, workflow_tag, currentUser);
						} else {
							removeTag(reference, workflow_tag, currentUser);
						}
						succeeded = true;
					} catch (FatalAPIException fe) {
						throw fe;
					} catch (APIException ae) {
						retries++;
						if (retries > maxRetries) {
							throw ae;
						}
						try {
							Thread.sleep(retryDelay);
							log.info("UpdateBulk: Retry #" + retries + " for " + reference.getMgiid());
						} catch (InterruptedException ie) {
							throw new FatalAPIException("Operation cancelled - system interrupted");
						}
					}
				}
			} else {
				throw new FatalAPIException("Unknown reference key: " + refsKey);
			}
		}
	}


	/***--- (private) instance methods ---***/

	/* return a single Term matching the parameters encoded as a Map in the given JSON string
	 */
	private Term getTerm (String json) throws FatalAPIException {
		MapMaker mapMaker = new MapMaker();
				
		try {
			SearchResults<Term> terms = termDAO.search(mapMaker.toMap(json));

			if ((terms.items == null) || (terms.items.size() == 0)) {
				throw new FatalAPIException("ReferenceRepository: Could not find term for JSON: " + json);
			} else if (terms.items.size() > 1) {
				throw new FatalAPIException("ReferenceRepository: Found too many terms for JSON: " + json);
			}

			return terms.items.get(0);
		} catch (Exception e) {
			throw new FatalAPIException("ReferenceRepository: Term search failed: " + e.toString());
		}
	}

	/* return a single Term matching the given vocabulary / term pair
	 */
	private Term getTermByTerm (Integer vocabKey, String term) throws FatalAPIException {
		return getTerm("{\"_vocab_key\" : " + vocabKey + ", \"term\" : \"" + term + "\"}");
	}

	/* return a single Term matching the given vocabulary / abbreviation pair
	 */
	private Term getTermByAbbreviation (Integer vocabKey, String abbreviation) throws FatalAPIException {
		return getTerm("{\"_vocab_key\" : " + vocabKey + ", \"abbreviation\" : \"" + abbreviation + "\"}");
	}

	/* retrieve the Reference object with the given primaryKey
	 */
	private LTReference getReference(String primaryKey) throws FatalAPIException, APIException {
		if (primaryKey == null) {
			throw new FatalAPIException("ReferenceRepository.getReference() : reference key is null");
		}
		LTReference reference = referenceDAO.getReference(primaryKey);
		if (reference == null) {
			throw new FatalAPIException("ReferenceRepository.getReference(): Unknown reference key: " + primaryKey);
		}
		return reference;
	}

	/* take the data from the domain object and overwrite any changed data into the entity object
	 * (assumes we are working in a transaction and persists any sub-objects into the database, but does
	 * not persist this Reference object itself, as other changes could be coming)
	 */
	private void applyDomainChanges(LTReference entity, LTReferenceDomain domain, User currentUser) throws NonFatalAPIException, APIException {
		// Note that we must have 'anyChanges' after the OR, otherwise short-circuit evaluation will only save
		// the first section changed.

		boolean anyChanges = applyStatusChanges(entity, domain, currentUser);
		anyChanges = applyTagChanges(entity, domain, currentUser) || anyChanges;
		anyChanges = applyBasicFieldChanges(entity, domain, currentUser) || anyChanges;
		anyChanges = applyBookChanges(entity, domain, currentUser) || anyChanges;
		anyChanges = applyNoteChanges(entity, domain, currentUser) | anyChanges;
		anyChanges = applyAccessionIDChanges(entity, domain, currentUser) || anyChanges;
		anyChanges = applyWorkflowDataChanges(entity, domain, currentUser) || anyChanges;
		anyChanges = applyAlleleAssocChanges(entity, domain.getAlleleAssocs(), currentUser) || anyChanges;		
		anyChanges = applyStrainAssocChanges(entity, domain.getStrainAssocs(), currentUser) || anyChanges;		
		anyChanges = applyMarkerAssocChanges(entity, domain.getMarkerAssocs(), currentUser) || anyChanges;

		if (anyChanges) {
			entity.setModificationInfo(currentUser);
		}
	}

	/* handle the basic fields that have changed between this Reference and the given ReferenceDomain
	 */
	private boolean applyBasicFieldChanges(LTReference entity, LTReferenceDomain domain, User currentUser) throws FatalAPIException {
		// exactly one set of basic data per reference, including:  isDiscard flag, reference type,
		// author, primary author (derived), journal, title, volume, issue, date, year, pages, 
		// abstract, and isReviewArticle flag

		boolean anyChanges = false;

		// determine if the isDiscard flag is set in the ReferenceDomain object
		int rdDiscard = 0;
		if ("1".equals(domain.isDiscard) || ("Yes".equalsIgnoreCase(domain.isDiscard))) {
			rdDiscard = 1;
		}

		// determine if the isReviewArticle flag is set in the ReferenceDomain object
		int rdReview = 0;
		if ("1".equals(domain.isReviewArticle) || ("Yes".equalsIgnoreCase(domain.isReviewArticle))) {
			rdReview = 1;
		}

		Integer year = null;
		try {
			year = Integer.parseInt(domain.year);
		} catch (NumberFormatException p) {
			throw new FatalAPIException("Year is not an integer: " + domain.year);
		}

		String refTypeKey = String.valueOf(entity.getReferenceTypeTerm().get_term_key());
		
		// update this object's data to match what was passed in
		if ((rdDiscard != entity.getIsDiscard()) || (rdReview != entity.getIsReviewArticle())
				|| !smartEqual(entity.getAuthors(), domain.authors)
				|| !smartEqual(entity.getJournal(), domain.journal)
				|| !smartEqual(entity.getTitle(), domain.title)
				|| !smartEqual(entity.getVol(), domain.vol)
				|| !smartEqual(entity.getIssue(), domain.issue)
				|| !smartEqual(entity.getDate(), domain.date)
				|| !smartEqual(entity.getYear(), year)
				|| !smartEqual(refTypeKey, domain.referenceTypeKey)
				|| !smartEqual(entity.getPgs(), domain.pgs)
				|| !smartEqual(entity.getReferenceAbstract(), domain.referenceAbstract)
				) {

			if (domain.authors != null) {
				Pattern pattern = Pattern.compile("([^;]+).*");		// any characters up to the first semicolon are the primary author
				Matcher matcher = pattern.matcher(domain.authors);
				if (matcher.find()) {
					entity.setPrimary_author(matcher.group(1));
				}
			}

			entity.setIsDiscard(rdDiscard);
			entity.setIsReviewArticle(rdReview);
			entity.setAuthors(domain.authors);
			entity.setJournal(domain.journal);
			entity.setTitle(domain.title);
			entity.setVol(domain.vol);
			entity.setIssue(domain.issue);
			entity.setDate(domain.date);
			entity.setYear(year);
			entity.setPgs(domain.pgs);
			entity.setReferenceTypeTerm(termDAO.get(Integer.valueOf(domain.getReferenceTypeKey())));
			entity.setReferenceAbstract(DecodeString.setDecodeToLatin9(domain.referenceAbstract));			
			entity.setModificationInfo(currentUser);
			
			anyChanges = true;
		}

		return anyChanges;
	}

	/* comparison function that handles null values well
	 */
	private boolean smartEqual(Object a, Object b) {
		if (a == null) {
			if (b == null) { return true; }
			else { return false; }
		}		
		return a.equals(b);
	}

	// remove any (optional) prefix from DOI ID
	private String cleanDoiID(String doiID) {
		// all DOI IDs must begin with "10.", but if not, just trust the user
		if ((doiID != null) && (!doiID.startsWith("10."))) {
			int tenPosition = doiID.indexOf("10.");
			if (tenPosition < 0) {
				return doiID;
			}
			doiID = doiID.substring(tenPosition);
		}
		return doiID;
	}

	// remove any (optional) prefix from PubMed ID
	private String cleanPubMedID(String pubmedID) {
		// all PubMed IDs are purely numeric, so strip off anything to the left of the first non-numeric character
		if ((pubmedID != null) && (pubmedID.trim().length() > 0) && (!pubmedID.matches("^[0-9]+$"))) {
			// anything up to the final non-digit, followed by the digits that are the PubMed ID
			Pattern p = Pattern.compile("^.*[^0-9]+([0-9]+)$");
			Matcher m = p.matcher(pubmedID.trim());
			if (m.find()) {
				pubmedID = m.group(1);
			} else {
				// if there are letters after the digits, just keep the original ID (don't throw an exception)
				return pubmedID;
			}
		}
		return pubmedID;
	}

	/* apply ID changes from domain to entity for PubMed, DOI, and GO REF IDs
	 */
	private boolean applyAccessionIDChanges(LTReference entity, LTReferenceDomain domain, User currentUser) {
		// assumes only one ID per reference for each logical database (valid assumption, August 2017)
		// need to handle:  new ID for logical db, updated ID for logical db, deleted ID for logical db

		// do any cleanup of DOI ID and PubMed ID first

		domain.doiid = cleanDoiID(domain.doiid);
		domain.pubmedid = cleanPubMedID(domain.pubmedid);

		boolean anyChanges = false;
		Pattern pattern = Pattern.compile("(.*?)([0-9]+)");		// any characters as a prefix (reluctant group), followed by one or more digits

		if (!smartEqual(entity.getDoiid(), domain.doiid)) {
			String prefixPart = domain.doiid;					// defaults
			Integer numericPart = null;

			if (domain.doiid != null) {
				Matcher m = pattern.matcher(domain.doiid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Integer.parseInt(m.group(2));
				}
			}

			anyChanges = applyOneIDChange(entity, Constants.LDB_DOI, domain.doiid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, currentUser) || anyChanges;
		}

		if (!smartEqual(entity.getPubmedid(), domain.pubmedid)) {
			String prefixPart = domain.pubmedid;				// defaults
			Integer numericPart = null;

			if (domain.pubmedid != null) {
				Matcher m = pattern.matcher(domain.pubmedid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Integer.parseInt(m.group(2));
				}
			}

			anyChanges = applyOneIDChange(entity, Constants.LDB_PUBMED, domain.pubmedid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, currentUser) || anyChanges;
		}

		if (!smartEqual(entity.getGorefid(), domain.gorefid)) {
			String prefixPart = domain.gorefid;					// defaults
			Integer numericPart = null;

			if (domain.gorefid != null) {
				Matcher m = pattern.matcher(domain.gorefid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Integer.parseInt(m.group(2));
				}
			}

			anyChanges = applyOneIDChange(entity, Constants.LDB_GOREF, domain.gorefid, prefixPart, numericPart, Constants.SECONDARY, Constants.PRIVATE, currentUser) || anyChanges;
		}
		
		// if entity contains a jnum and domain does not, ok to try and delete it
		if (entity.getJnumid() != null && 
				(domain.jnumid == null || domain.jnumid.isEmpty())) {
			
			String prefixPart = domain.jnumid;				// defaults
			Integer numericPart = null;

			if (domain.jnumid != null) {
				Matcher m = pattern.matcher(domain.jnumid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Integer.parseInt(m.group(2));
				}
			}

			anyChanges = applyOneIDChange(entity, Constants.LDB_JNUM, domain.jnumid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, currentUser) || anyChanges;
		}
		
		return anyChanges;
	}

	/* Apply a single ID change to this reference.  If there already is an ID for this logical database, replace it.  If there wasn't
	 * one, add one.  And, if there was one previously, but there's not now, then delete it.
	 */
	private boolean applyOneIDChange(LTReference entity, Integer ldb, String accID, String prefixPart, Integer numericPart, Integer preferred, Integer isPrivate, User currentUser) {
		// first parameter is required; bail out if it is null
		if (ldb == null) { return false; }
		
		// First, need to find any existing AccessionID object for this logical database.

		List<Accession> ids = entity.getAccessionIDs();
		int idPos = -1;			// position of correct ID in list of IDs
		for (int i = 0; i < ids.size(); i++) {
			Accession myID = ids.get(i);
			
			// skip if MGI:xxxx; J:xxxx is OK
			if (ldb.equals(1)) {
				if (myID.getPrefixPart().equals("MGI:")) {
					continue;
				}
			}
			
			if (ldb.equals(myID.getLogicaldb().get_logicaldb_key())) {
				idPos = i;
				break;	}
		}

		// If we had a previous ID for this logical database, we either need to modify it or delete it.
		if (idPos >= 0) {
			// Passing in a null ID indicates that any existing ID should be removed.
			if ( (accID == null) || (accID.trim().length() == 0))  {
				referenceDAO.remove(ids.get(idPos));
			} else {
				// Otherwise, we can update the ID and other data for this logical database.
				Accession myID = ids.get(idPos);
				myID.setAccID(accID);
				myID.setIsPrivate(isPrivate);
				myID.setPreferred(preferred);
				myID.setPrefixPart(prefixPart);
				myID.setNumericPart(numericPart);
				myID.setModification_date(new Date());
				myID.setModifiedBy(currentUser);
			}
		} else {
			// We didn't find an existing ID for this logical database, so we need to add one.

			Date creation = new Date();

			Accession myID = new Accession();
			myID.set_accession_key(accessionDAO.getNextKey());
			myID.setAccID(accID);
			myID.setPreferred(preferred);
			myID.setIsPrivate(isPrivate);
			myID.setLogicaldb(logicaldbDAO.get(ldb));
			myID.set_object_key(entity.get_refs_key());
			myID.setMgiType(mgiTypeDAO.get(Constants.TYPE_REFERENCE));
			myID.setPrefixPart(prefixPart);
			myID.setNumericPart(numericPart);
			myID.setCreation_date(creation);
			myID.setModification_date(creation);
			myID.setCreatedBy(currentUser);
			myID.setModifiedBy(currentUser);

			referenceDAO.persist(myID);
		}
		return true;
	}

	/* apply any changes from domain to entity for the reference notes field
	 */
	private boolean applyNoteChanges(LTReference entity, LTReferenceDomain domain, User currentUser) {
		// at most one note per reference
		// need to handle:  new note, updated note, deleted note

		boolean anyChanges = false;
		boolean hadNote = entity.getNotes().size() > 0;
		boolean willHaveNote = (domain.referenceNote != null) && (domain.referenceNote.length() > 0);
		
		if (hadNote && willHaveNote) {
			// already have a note and will continue to have a note; just need to apply any difference

			ReferenceNote note = entity.getNotes().get(0);
			if (!smartEqual(note.getNote(), domain.referenceNote)) {
				note.setNote(DecodeString.setDecodeToLatin9(domain.referenceNote).replace("''",  "'"));
				anyChanges = true;
			}

		} else if (hadNote) {
			// had a note previously, but it needs to be deleted, because reference now has no note

			referenceDAO.remove(entity.getNotes().get(0));
			anyChanges = true;

		} else if (willHaveNote) {
			// did not have a note previously, but now need to create one

			ReferenceNote note = new ReferenceNote();
			note.set_refs_key(entity.get_refs_key());
			note.setNote(DecodeString.setDecodeToLatin9(domain.referenceNote).replace("''",  "'"));			
			note.setCreation_date(new Date());
			note.setModification_date(note.getCreation_date()); 
			referenceDAO.persist(note);
			entity.getNotes().add(note);
			anyChanges = true;
		}
			
		return anyChanges;
	}

	/* apply changes to book data fields from domain to entity
	 */
	private boolean applyBookChanges(LTReference entity, LTReferenceDomain domain, User currentUser) {
		// at most one set of book data per reference
		// need to handle:  deleted book data, updated book data, new book data

		boolean anyChanges = false;
		boolean wasBook = "Book".equalsIgnoreCase(entity.getReferenceType());
		boolean willBeBook = "Book".equalsIgnoreCase(domain.referenceType);

		// If this reference is already a book and will continue to be a book, need to apply
		// any changes to the fields of the existing book data.
		if (wasBook && willBeBook && (entity.getReferenceBook().size() > 0)) {
			ReferenceBook book = entity.getReferenceBook().get(0);

			if (!smartEqual(book.getBook_author(), domain.book_author) || !smartEqual(book.getBook_title(), domain.book_title) || 
					!smartEqual(book.getPlace(), domain.place) || !smartEqual(book.getPublisher(), domain.publisher) ||
					!smartEqual(book.getSeries_ed(), domain.series_ed)) {

				book.setBook_author(domain.book_author);
				book.setBook_title(domain.book_title);
				book.setPlace(domain.place);
				book.setPublisher(domain.publisher);
				book.setSeries_ed(domain.series_ed);
				book.setModification_date(new Date());
				anyChanges = true;
			}

		} else if (wasBook && (entity.getReferenceBook().size() > 0)) {
			// This reference was a book previously, but its type has changed, so need to delete book-specific data.

			referenceDAO.remove(entity.getReferenceBook().get(0));
			anyChanges = true;

		} else if (willBeBook) {
			// This reference was not a book previously, but now will be, so we need to add book-specific data.

			ReferenceBook book = new ReferenceBook();			
			book.set_refs_key(entity.get_refs_key());
			book.setBook_author(domain.book_author);
			book.setBook_title(domain.book_title);
			book.setPlace(domain.place);
			book.setPublisher(domain.publisher);
			book.setSeries_ed(domain.series_ed);			
			book.setCreation_date(new Date());
			book.setModification_date(book.getCreation_date()); 

			referenceDAO.persist(book);
			entity.getReferenceBook().add(book);
			anyChanges = true;
		}
		return anyChanges;
	}

	/* apply changes in workflow data fields (not status, though) from domain to entity
	 */
	private boolean applyWorkflowDataChanges(LTReference entity, LTReferenceDomain domain, User currentUser) throws APIException {
		// at most one set of workflow data per reference
		// need to handle:  updated workflow data, new workflow data -- (no deletions)

		boolean anyChanges = false;
		LTReferenceWorkflowData myWD = entity.getWorkflowData();

		if (myWD != null) {
			// Compare fields and update if needed.  We do not update the extracted text and the
			// has-PDF flag, as those are updated by other processes.

			if (!smartEqual(myWD.getSupplemental(), domain.has_supplemental)
					|| !smartEqual(myWD.getLink_supplemental(), domain.link_to_supplemental)) {

				myWD.setSupplementalTerm(getTermByTerm(Constants.VOC_SUPPLEMENTAL, domain.has_supplemental));
				myWD.setLink_supplemental(domain.link_to_supplemental);
				myWD.setModifiedByUser(currentUser);
				myWD.setModification_date(new Date());
				anyChanges = true;
			}

		} else {
			// For some reason, no workflow data record exists.  So, create one.

			myWD = new LTReferenceWorkflowData();
			myWD.set_refs_key(Integer.valueOf(domain.refsKey));
			myWD.setHas_pdf(0);
			myWD.setSupplementalTerm(getTermByTerm(Constants.VOC_SUPPLEMENTAL, domain.has_supplemental));
			myWD.setLink_supplemental(domain.link_to_supplemental);
			myWD.setExtracted_text(null);
			myWD.setCreatedByUser(currentUser);
			myWD.setModifiedByUser(currentUser);
			myWD.setCreation_date(new Date());
			myWD.setModification_date(myWD.getCreation_date()); 

			referenceDAO.persist(myWD);
			entity.setWorkflowData(myWD);
			anyChanges = true;
		}

		return anyChanges;
	}

	/* handle removing/adding any workflow tags that have changed between the Reference and the passed-in
	 * ReferenceDomain.  Persists any tag changes to the database.  Returns true if any changes were made,
	 * false otherwise.
	 */
	private boolean applyTagChanges(LTReference entity, LTReferenceDomain domain, User currentUser) throws NonFatalAPIException, APIException {
		// short-circuit method if no tags in Reference or in ReferenceDomain
		if ((entity.getWorkflowTagsAsStrings().size() == 0) && (domain.workflow_tags.size() == 0)) {
			return false;
		}

		// set of tags specified in domain object -- potentially to add to object
		Set<String> toAdd = new HashSet<String>();
		for (String rdTag : domain.workflow_tags) {
			toAdd.add(rdTag.trim());
		}

		// list of tags that need to be removed from this object
		List<LTReferenceWorkflowTag> toDelete = new ArrayList<LTReferenceWorkflowTag>();

		// Now we need to diff the set of tags we already have and the set of tags to potentially add. Anything
		// left in toAdd will need to be added as a new tag, and anything in toDelete will need to be removed.

		for (LTReferenceWorkflowTag refTag : entity.getWorkflowTags()) {
			String myTag = refTag.getTag().getTerm();

			// matching tags
			if (toAdd.contains(myTag)) {
				// already have this one, don't need to add it
				toAdd.remove(myTag);
			} else {
				// current one isn't in the new list from domain object, so need to remove it
				toDelete.add(refTag);
			}
		}

		// remove defunct tags

		for (LTReferenceWorkflowTag rwTag : toDelete) {
			// would like to do this here, but it fails due to a null _refs_key in the table:
			//		this.workflowTags.remove(rwTag);
			referenceDAO.remove(rwTag);
		}

		// add new tags (use shared method, as this will be useful when adding tags to batches of references)

		for (String rdTag : toAdd) {
			addTag(entity, rdTag, currentUser);
		}

		return (toDelete.size() > 0) || (toAdd.size() > 0);
	}

	/* shared method for adding a workflow tag to a Reference
	 */
	public void addTag(LTReference entity, String rdTag, User currentUser) throws NonFatalAPIException, APIException {
		// if we already have this tag applied, skip it (extra check needed for batch additions to avoid
		// adding duplicates)

		String trimTag = rdTag.trim();
		for (LTReferenceWorkflowTag refTag : entity.getWorkflowTags()) {
			if (trimTag.equals(refTag.getTag().getTerm()) ) {
				return;
			}
		}

		// need to find the term of the tag, wrap it in an association, persist the association, and
		// add it to the workflow tags for this Reference

		Term tagTerm = getTermByTerm(Constants.VOC_WORKFLOW_TAGS, rdTag);
		if (tagTerm != null) {

			LTReferenceWorkflowTag rwTag = new LTReferenceWorkflowTag();
			rwTag.set_assoc_key(referenceDAO.getNextWorkflowTagKey());
			rwTag.set_refs_key(entity.get_refs_key());
			rwTag.setTag(tagTerm);
			rwTag.setCreatedByUser(currentUser);
			rwTag.setModifiedByUser(rwTag.getCreatedByUser());
			rwTag.setCreation_date(new Date());
			rwTag.setModification_date(rwTag.getCreation_date());
			try {
				referenceDAO.persist(rwTag);
			} catch (Exception e) {
				throw new NonFatalAPIException("Cannot add tag: " + e.toString());
			}

			entity.getWorkflowTags().add(rwTag);
			entity.setModificationInfo(currentUser);

		}
	}

	/* shared method for removing a workflow tag from a Reference (no-op if this ref doesn't have the tag)
	 */
	public void removeTag(LTReference entity, String rdTag, User currentUser) throws APIException {
		if (entity.getWorkflowTags() == null) { return; }

		String lowerTag = rdTag.toLowerCase().trim();
		for (LTReferenceWorkflowTag refTag : entity.getWorkflowTags()) {
			if (lowerTag.equals(refTag.getTag().getTerm().toLowerCase()) ) {
				referenceDAO.remove(refTag);
				entity.setModificationInfo(currentUser);
				return;
			}
		}
	}

	/* convenience method, used by applyStatusChanges() to reduce redundant code in setting workflow
	 * group statuses.  returns true if an update was made, false if no change.  persists any changes
	 * to the database.
	 */
	private boolean updateStatus(LTReference entity, String groupAbbrev, String currentStatus, String newStatus, User currentUser) throws NonFatalAPIException, APIException {
		// no update if new status matches old status (or if no group is specified)
		if ( ((currentStatus != null) && currentStatus.equals(newStatus)) || (groupAbbrev == null) ||
				((currentStatus == null) && (newStatus == null)) ) {
			return false;
		}

		// At this point, we know we have a status update.  If there was an existing record, we need
		// to flag it as not current.
		if (currentStatus != null) {
			for (LTReferenceWorkflowStatus rws : entity.getWorkflowStatuses()) {
				if ( (rws.getIsCurrent() == 1) && groupAbbrev.equals(rws.getGroupAbbreviation()) ) {
					rws.setIsCurrent(0);
					break;				// no more can match, so exit the loop
				}
			}
		}

		// Now we need to add a new status record for this change -- and need to persist this new object to the
		// database explicitly, before the whole reference gets persisted later on.

		LTReferenceWorkflowStatus newRws = new LTReferenceWorkflowStatus();
		newRws.set_assoc_key(referenceDAO.getNextWorkflowStatusKey());
		newRws.set_refs_key(entity.get_refs_key());
		newRws.setIsCurrent(1);
		newRws.setGroupTerm(getTermByAbbreviation(Constants.VOC_WORKFLOW_GROUP, groupAbbrev));
		newRws.setStatusTerm(getTermByTerm(Constants.VOC_WORKFLOW_STATUS, newStatus));
		newRws.setCreatedByUser(currentUser);
		newRws.setModifiedByUser(newRws.getCreatedByUser());
		newRws.setCreation_date(new Date());
		newRws.setModification_date(newRws.getCreation_date());
		entity.getWorkflowStatuses().add(newRws);

		try {
			referenceDAO.persist(newRws);
		} catch (Exception e) {
			throw new NonFatalAPIException("Could not save status change: " + e.toString());
		}

		return true;
	}

	/* handle applying any status changes for workflow groups.  If a group in 'domain' has a different status
	 * from what's in the entity, we need:
	 * 		a. the old status to be changed so isCurrent = 0, and
	 * 		b. a new ReferenceWorkflowStatus object created and set to be current
	 * As well, if this Reference has no J: number and we just assigned a status other than "Not Routed", 
	 * then we assign the next available J: number to this reference.
	 */
	private boolean applyStatusChanges(LTReference entity, LTReferenceDomain domain, User currentUser) throws NonFatalAPIException, APIException {
		// note that we need to put 'anyChanges' last for each OR pair, otherwise short-circuit evaluation
		// will only let the first change go through and the rest will not execute.

		boolean anyChanges = updateStatus(entity, Constants.WG_AP, entity.getStatus(Constants.WG_AP), domain.ap_status, currentUser);
		anyChanges = updateStatus(entity, Constants.WG_GO, entity.getStatus(Constants.WG_GO), domain.go_status, currentUser) || anyChanges;
		anyChanges = updateStatus(entity, Constants.WG_GXD, entity.getStatus(Constants.WG_GXD), domain.gxd_status, currentUser) || anyChanges;
		anyChanges = updateStatus(entity, Constants.WG_QTL, entity.getStatus(Constants.WG_QTL), domain.qtl_status, currentUser) || anyChanges;
		anyChanges = updateStatus(entity, Constants.WG_TUMOR, entity.getStatus(Constants.WG_TUMOR), domain.tumor_status, currentUser) || anyChanges;

		if (anyChanges) {
			entity.clearWorkflowStatusCache();

			// if we had a status change, if at least one status is not "Not Routed", and if the reference
			// doesn't already have a J#, we need to create one

			if (entity.getJnumid() == null) {
				boolean anyNotRouted = false;
				for (String workgroup : Constants.WG_ALL) {
					String wgStatus = entity.getStatus(workgroup);
					if ((wgStatus != null) && !wgStatus.equals(Constants.WS_NOT_ROUTED)) {
						anyNotRouted = true;
						break;
					}
				}

				if (anyNotRouted) {
					try {
						log.info("Assigning new J: number");
						referenceDAO.assignNewJnumID(String.valueOf(entity.get_refs_key()), currentUser.get_user_key());
						log.info(" - finished");
					} catch (Exception e) {
						log.info("Caught exception: " + e.toString());
						throw new NonFatalAPIException("Failed to assign J: number");
					}
				}
			} // if no J#
		}
		return anyChanges;
	}

	/* apply any changes from domain to entity for the reference/allele associations
	 */
	private boolean applyAlleleAssocChanges(LTReference entity, List<MGIReferenceAlleleAssocDomain> domain, User currentUser) {
		// referenceAssocService will handle add (c), delete (d)

		boolean anyChanges = false;

		if (domain != null) {
			if (referenceAssocService.processAlleleAssoc(domain, currentUser)) {
				anyChanges = true;
			}
		}
		
		return anyChanges;
	}

	/* apply any changes from domain to entity for the reference/strain associations
	 */
	private boolean applyStrainAssocChanges(LTReference entity, List<MGIReferenceStrainAssocDomain> domain, User currentUser) {
		// referenceAssocService will handle add (c), delete (d)

		boolean anyChanges = false;

		if (domain != null) {
			if (referenceAssocService.processStrainAssoc(domain, currentUser)) {
				anyChanges = true;
			}
		}
		
		return anyChanges;
	}

	/* apply any changes from domain to entity for the reference/marker associations
	 */
	private boolean applyMarkerAssocChanges(LTReference entity, List<MGIReferenceMarkerAssocDomain> domain, User currentUser) {
		// referenceAssocService will handle add (c), delete (d)

		boolean anyChanges = false;

		if (domain != null) {
			if (referenceAssocService.processMarkerAssoc(domain, currentUser)) {
				anyChanges = true;
			}
		}
		
		return anyChanges;
	}
		
}
