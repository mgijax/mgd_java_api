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
import javax.persistence.Query;

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
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowDataDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowRelevanceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowTag;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceBookService;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceNoteService;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceWorkflowDataService;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceWorkflowRelevanceService;
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
import org.jboss.logging.Logger;


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
	@Inject
	private ReferenceBookService bookService;
	@Inject
	private ReferenceNoteService noteService;
//	@Inject
//	private ReferenceWorkflowStatusService statusService;	
	@Inject
	private ReferenceWorkflowRelevanceService relevanceService;
	@Inject
	private ReferenceWorkflowDataService dataService;
//	@Inject
//	private AccessionService accessionService;
	
	LTReferenceTranslator translator = new LTReferenceTranslator();

	private Logger log = Logger.getLogger(getClass());

	/* These work together to allow for a maximum delay of two seconds for retries: */
	private static int maxRetries = 10;		// maximum number of retries for non-fatal exceptions on update operations
	private static int retryDelay = 200;	// number of ms to wait before retrying update operation after non-fatal exception

	/***--- (public) instance methods ---***/

	/* gets a ReferenceDomain object that is fully fleshed out from a Reference
	 */
	@Override
	public LTReferenceDomain get(String key) throws FatalAPIException, APIException {
		log.info("LTReferenceDomain get(String key)");
		LTReference entity = referenceDAO.get(Integer.valueOf(key));
		LTReferenceDomain domain = translator.translate(entity);
		return domain;	
	}

	@Override
	public SearchResults<LTReferenceDomain> search(Map<String,Object> params) {
		log.info("search(Map<String,Object> params");
		
		SearchResults<LTReference> refs = referenceDAO.search(params);
		SearchResults<LTReferenceDomain> results = new SearchResults<LTReferenceDomain>();

		results.elapsed_ms = refs.elapsed_ms;
		results.error = refs.error;
		results.message = refs.message;
		results.status_code = refs.status_code;
		results.total_count = refs.total_count;
		results.all_match_count = refs.all_match_count;

		if (refs.items != null) {
			// translate each search results into domain
			results.items = new ArrayList<LTReferenceDomain>();
			for (LTReference ref : refs.items) {
				results.items.add(translator.translate(ref));
			}
		}
		
		return results;
	}

	@Override
	public LTReferenceDomain update(LTReferenceDomain domain, User user) throws FatalAPIException, NonFatalAPIException, APIException {
		log.info("LTReferenceDomain update(LTReferenceDomain domain, User user)");
		LTReference entity = referenceDAO.get(Integer.valueOf(domain.getRefsKey()));
		applyChanges(entity, domain, user);
		referenceDAO.persist(entity);				
		Query query = referenceDAO.createNativeQuery("select count(*) from BIB_reloadCache(" + domain.getRefsKey() + ")");
		query.getResultList();
		return null;	// just return null, will look up later on
	}

	/* set the given workflow_tag for all references identified in the list of keys
	 */
	public void updateInBulk(List<String> refsKey2, String workflow_tag, String workflow_tag_operation, User user) throws FatalAPIException, APIException {

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
			LTReference entity = referenceDAO.get(Integer.valueOf(refsKey));

			if (entity != null) {
				int retries = 0;
				boolean succeeded = false;

				while (!succeeded) {
					try {
						if (workflow_tag_operation.equals(Constants.OP_ADD_WORKFLOW)) {
							addTag(entity, workflow_tag, user);
						} else {
							removeTag(entity, workflow_tag, user);
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
							log.info("UpdateBulk: Retry #" + retries + " for " + entity.getMgiid());
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

	/* take the data from the domain object and overwrite any changed data into the entity object
	 * (assumes we are working in a transaction and persists any sub-objects into the database, but does
	 * not persist this Reference object itself, as other changes could be coming)
	 */
	private void applyChanges(LTReference entity, LTReferenceDomain domain, User user) throws NonFatalAPIException, APIException {
		// Note that we must have 'anyChanges' after the OR, otherwise short-circuit evaluation will only save
		// the first section changed.

		boolean anyChanges;
		
		anyChanges = applyBasicFieldChanges(entity, domain, user);
		anyChanges = applyWorkflowStatusChanges(entity, domain, user) || anyChanges;
		anyChanges = applyWorkflowTagChanges(entity, domain, user) || anyChanges;
		anyChanges = applyBookChanges(entity, domain, user) || anyChanges;		// uses ReferenceDomainService()
		anyChanges = applyNoteChanges(entity, domain, user) | anyChanges;		// uses ReferenceNoteService()
		anyChanges = applyAccessionIDChanges(entity, domain, user) || anyChanges;
		anyChanges = applyWorkflowDataChanges(entity, domain, user) || anyChanges;
		anyChanges = applyWorkflowRelevanceChanges(entity, domain, user) || anyChanges;   			// uses relevanceService
		anyChanges = applyAlleleAssocChanges(entity, domain.getAlleleAssocs(), user) || anyChanges;	// uses referenceAssocService	
		anyChanges = applyStrainAssocChanges(entity, domain.getStrainAssocs(), user) || anyChanges;	// uses referenceAssocService	
		anyChanges = applyMarkerAssocChanges(entity, domain.getMarkerAssocs(), user) || anyChanges;	// uses referenceAssocService

		if (anyChanges) {
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());
		}
	}

	/* handle the basic fields that have changed between this Reference and the given ReferenceDomain
	 */
	private boolean applyBasicFieldChanges(LTReference entity, LTReferenceDomain domain, User user) throws FatalAPIException {
		// exactly one set of basic data per reference, including:  reference type,
		// author, primary author (derived), journal, title, volume, issue, date, year, pages, 
		// abstract, and isReviewArticle flag

		log.info("applyBasicFieldChanges()");
		
		boolean anyChanges = false;

		// determine if the isReviewArticle flag is set in the ReferenceDomain object
		int rdReview = 0;
		if ("1".equals(domain.isReviewArticle)) {
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
		if ((rdReview != entity.getIsReviewArticle())
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

			entity.setIsReviewArticle(rdReview);
			
			if (domain.getAuthors() == null || domain.getAuthors().isEmpty()) {
				entity.setAuthors(null);
				entity.setPrimaryAuthor(null);
			}
			else {
				entity.setAuthors(domain.authors);
				String[] authors = domain.getAuthors().split(";");
				entity.setPrimaryAuthor(authors[0]);				
			}

			if (domain.getJournal() == null || domain.getJournal().isEmpty()) {
				entity.setJournal(null);
			}
			else {
				entity.setJournal(domain.journal);
			}
			
			if (domain.getTitle() == null || domain.getTitle().isEmpty()) {
				entity.setTitle(null);
			}
			else {
				entity.setTitle(domain.journal);
			}			

			if (domain.getVol() == null || domain.getVol().isEmpty()) {
				entity.setVol(null);
			}
			else {
				entity.setVol(domain.journal);
			}			
			
			if (domain.getIssue() == null || domain.getIssue().isEmpty()) {
				entity.setIssue(null);
			}
			else {
				entity.setIssue(domain.journal);
			}			
			
			if (domain.getPgs() == null || domain.getPgs().isEmpty()) {
				entity.setPgs(null);
			}
			else {
				entity.setPgs(domain.journal);
			}			
						
			entity.setDate(domain.date);
			entity.setYear(year);
			entity.setReferenceTypeTerm(termDAO.get(Integer.valueOf(domain.getReferenceTypeKey())));
			
			if (domain.getReferenceAbstract() == null || domain.getReferenceAbstract().isEmpty()) {
				entity.setReferenceAbstract(null);
			}
			else {
				entity.setReferenceAbstract(DecodeString.setDecodeToLatin9(domain.referenceAbstract).replace("''",  "'"));			
			}
			
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());
			
			anyChanges = true;
		}

		return anyChanges;
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
	private boolean applyAccessionIDChanges(LTReference entity, LTReferenceDomain domain, User user) {
		// assumes only one ID per reference for each logical database (valid assumption, August 2017)
		// need to handle:  new ID for logical db, updated ID for logical db, deleted ID for logical db

		// do any cleanup of DOI ID and PubMed ID first

		log.info("applyAccessionIDChanges()");
		
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

			anyChanges = applyOneIDChange(entity, Constants.LDB_DOI, domain.doiid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, user) || anyChanges;
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

			anyChanges = applyOneIDChange(entity, Constants.LDB_PUBMED, domain.pubmedid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, user) || anyChanges;
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

			anyChanges = applyOneIDChange(entity, Constants.LDB_GOREF, domain.gorefid, prefixPart, numericPart, Constants.SECONDARY, Constants.PRIVATE, user) || anyChanges;
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

			anyChanges = applyOneIDChange(entity, Constants.LDB_JNUM, domain.jnumid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, user) || anyChanges;
		}
		
		return anyChanges;
	}

	/* Apply a single ID change to this reference.  If there already is an ID for this logical database, replace it.  If there wasn't
	 * one, add one.  And, if there was one previously, but there's not now, then delete it.
	 */
	private boolean applyOneIDChange(LTReference entity, Integer ldb, String accID, String prefixPart, Integer numericPart, Integer preferred, Integer isPrivate, User user) {
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

		// convert to:
		//if (accessionService.process(domain.getRefsKey(), domain.getEditAccessionIds(), "1", user)) {
		
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
				myID.setModifiedBy(user);
			}
		} else {
			// We didn't find an existing ID for this logical database, so we need to add one.
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
			myID.setCreation_date(new Date());
			myID.setModification_date(new Date());
			myID.setCreatedBy(user);
			myID.setModifiedBy(user);
			referenceDAO.persist(myID);
		}
		return true;
	}

	/* apply any changes from domain to entity for the reference notes 
	 */
	private boolean applyNoteChanges(LTReference entity, LTReferenceDomain domain, User user) {
		log.info("applyNoteChanges()");

		if (domain.getReferenceNote() == null) {
			return(false);
		}
		
		if (domain.getReferenceNote().getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
			if (domain.getReferenceNote().getNote().isEmpty()) {
				domain.getReferenceNote().setProcessStatus(Constants.PROCESS_DELETE);
			}
			else if (!smartEqual(entity.getReferenceNote().get(0).getNote(), domain.getReferenceNote().getNote())) {
				domain.getReferenceNote().setProcessStatus(Constants.PROCESS_UPDATE);				
			}
		}
		else {
			domain.getReferenceNote().setProcessStatus(Constants.PROCESS_CREATE);							
		}
		
		return(noteService.process(domain.getRefsKey(), domain.getReferenceNote(), user));		
	}

	/* apply any changes from domain to entity for the reference book 
	 */
	private boolean applyBookChanges(LTReference entity, LTReferenceDomain domain, User user) {
		log.info("applyBookChanges()");

		boolean isBookTerm = false;
		boolean isBookKey = false;
		
		if (domain.getReferenceType().equals("Book")) {
			isBookTerm = true;
		}
		if (domain.getReferenceTypeKey().equals("31576679")) {
			isBookKey = true;
		}
		
		log.info("isBookTerm:" + isBookTerm);
		log.info("isBookKey:" + isBookKey);
		
		if (isBookTerm && isBookKey) {
			log.info("applyBookChange/remain book");
			ReferenceBook book = entity.getReferenceBook().get(0);
			if (!smartEqual(book.getBook_au(), domain.getReferenceBook().getBook_author()) 
					|| !smartEqual(book.getBook_title(), domain.getReferenceBook().getBook_title()) 
					|| !smartEqual(book.getPlace(), domain.getReferenceBook().getPlace()) 
					|| !smartEqual(book.getPublisher(), domain.getReferenceBook().getPublisher()) 
					|| !smartEqual(book.getSeries_ed(), domain.getReferenceBook().getSeries_ed())) {
				domain.getReferenceBook().setProcessStatus(Constants.PROCESS_UPDATE);
			}
		} else if (isBookTerm) {
			log.info("applyBookChange/change from book to non-book");
			domain.getReferenceBook().setProcessStatus(Constants.PROCESS_DELETE);
		} else if (isBookKey) {
			log.info("applyBookChange/create book");
			domain.getReferenceBook().setProcessStatus(Constants.PROCESS_CREATE);
			domain.getReferenceBook().setRefsKey(domain.getRefsKey());			
		}
		
		return(bookService.process(domain.getRefsKey(), domain.getReferenceBook(), user));		
	}

	/* apply changes in workflow relevance from domain to entity
	 */
	private boolean applyWorkflowRelevanceChanges(LTReference entity, LTReferenceDomain domain, User user) throws APIException {
		// updated workflow relevance, new workflow relevance -- (no deletions)

		log.info("applyWorkflowRelevanceChanges()");
		
		// if relevance term has changed or user has changed
		if (!smartEqual(String.valueOf(entity.getWorkflowRelevance().get(0).getRelevanceTerm().get_term_key()), domain.getEditRelevanceKey())
			|| !smartEqual(entity.getWorkflowRelevance().get(0).getModifiedBy(), user.getLogin())
			) {

			// for each relevanceHistory, set processStatus = PROCESS_UPDATE, isCurrent = 0
			for (int i = 0; i < domain.getRelevanceHistory().size(); i++) {
				if (domain.getRelevanceHistory().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
					domain.getRelevanceHistory().get(i).setProcessStatus(Constants.PROCESS_UPDATE);
					domain.getRelevanceHistory().get(i).setIsCurrent("0");
				}
			}
			
			// add new relevance row
			ReferenceWorkflowRelevanceDomain newRelevance = new ReferenceWorkflowRelevanceDomain();
			newRelevance.setProcessStatus(Constants.PROCESS_CREATE);
			newRelevance.setRefsKey(domain.getRefsKey());
			newRelevance.setIsCurrent("1");
			newRelevance.setRelevanceKey(domain.getEditRelevanceKey());
			newRelevance.setConfidence(null);
			newRelevance.setVersion(null);
			domain.getRelevanceHistory().add(newRelevance);
		}
		return(relevanceService.process(domain.getRefsKey(), domain.getRelevanceHistory(), user));
	}
	

	/* apply changes in workflow data fields (not status, though) from domain to entity
	 */
	private boolean applyWorkflowDataChanges(LTReference entity, LTReferenceDomain domain, User user) throws APIException {
		// updates supplemental key which is stored in workflow data "body"
		
		log.info("applyWorkflowDataChanges()");
		
		if (entity.getWorkflowData().get(0) != null) {
			if (!smartEqual(String.valueOf(entity.getWorkflowData().get(0).getSupplementalTerm().get_term_key()), domain.getWorkflowData().getSupplementalKey())) {
				domain.getWorkflowData().setProcessStatus(Constants.PROCESS_UPDATE);
			}
		} else {
			// this should not happen, but if it does...create new "body"
			ReferenceWorkflowDataDomain newData = new ReferenceWorkflowDataDomain();
			newData.setProcessStatus(Constants.PROCESS_CREATE);
			newData.setRefsKey(domain.getRefsKey());
			newData.setSupplementalKey(domain.getWorkflowData().getSupplementalKey());
			domain.setWorkflowData(newData);
		}

		return(dataService.process(domain.getRefsKey(), domain.getWorkflowData(), user));
	}

	/* handle removing/adding any workflow tags that have changed between the Reference and the passed-in
	 * ReferenceDomain.  Persists any tag changes to the database.  Returns true if any changes were made,
	 * false otherwise.
	 */
	private boolean applyWorkflowTagChanges(LTReference entity, LTReferenceDomain domain, User user) throws NonFatalAPIException, APIException {

		log.info("applyWorkflowTagChanges()");
		
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
		List<ReferenceWorkflowTag> toDelete = new ArrayList<ReferenceWorkflowTag>();

		// Now we need to diff the set of tags we already have and the set of tags to potentially add. Anything
		// left in toAdd will need to be added as a new tag, and anything in toDelete will need to be removed.

		for (ReferenceWorkflowTag refTag : entity.getWorkflowTag()) {
			String myTag = refTag.getTagTerm().getTerm();

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
		for (ReferenceWorkflowTag rwTag : toDelete) {
			referenceDAO.remove(rwTag);
		}

		// add new tags (use shared method, as this will be useful when adding tags to batches of references)
		for (String rdTag : toAdd) {
			addTag(entity, rdTag, user);
		}

		return (toDelete.size() > 0) || (toAdd.size() > 0);
	}

	/* shared method for adding a workflow tag to a Reference
	 */
	public void addTag(LTReference entity, String rdTag, User user) throws NonFatalAPIException, APIException {
		// if we already have this tag applied, skip it (extra check needed for batch additions to avoid
		// adding duplicates)

		String trimTag = rdTag.trim();
		for (ReferenceWorkflowTag refTag : entity.getWorkflowTag()) {
			if (trimTag.equals(refTag.getTagTerm().getTerm()) ) {
				return;
			}
		}

		// need to find the term of the tag, wrap it in an association, persist the association, and
		// add it to the workflow tags for this Reference

		Term tagTerm = getTermByTerm(Constants.VOC_WORKFLOW_TAGS, rdTag);
		if (tagTerm != null) {
			ReferenceWorkflowTag rwTag = new ReferenceWorkflowTag();
			rwTag.set_refs_key(entity.get_refs_key());
			rwTag.setTagTerm(tagTerm);
			rwTag.setCreatedBy(user);
			rwTag.setModifiedBy(user);
			rwTag.setCreation_date(new Date());
			rwTag.setModification_date(new Date());
			try {
				referenceDAO.persist(rwTag);
			} catch (Exception e) {
				throw new NonFatalAPIException("Cannot add tag: " + e.toString());
			}

			entity.getWorkflowTag().add(rwTag);
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());
		}
	}

	/* shared method for removing a workflow tag from a Reference (no-op if this ref doesn't have the tag)
	 */
	public void removeTag(LTReference entity, String rdTag, User user) throws APIException {
		if (entity.getWorkflowTag() == null) { return; }

		String lowerTag = rdTag.toLowerCase().trim();
		for (ReferenceWorkflowTag refTag : entity.getWorkflowTag()) {
			if (lowerTag.equals(refTag.getTagTerm().getTerm().toLowerCase()) ) {
				referenceDAO.remove(refTag);
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				return;
			}
		}
	}

	/* convenience method, used by applyWorkflowStatusChanges() to reduce redundant code in setting workflow
	 * group statuses.  returns true if an update was made, false if no change.  persists any changes
	 * to the database.
	 */
	private boolean updateWorkflowStatus(LTReference entity, String groupAbbrev, String currentStatus, String newStatus, User user) throws NonFatalAPIException, APIException {

		// no update if new status matches old status (or if no group is specified)
		if ( ((currentStatus != null) && currentStatus.equals(newStatus)) || (groupAbbrev == null) ||
				((currentStatus == null) && (newStatus == null)) ) {
			return false;
		}

		// At this point, we know we have a status update.  
		// If there was an existing record, we need to flag it as not current.
		if (currentStatus != null) {
			for (ReferenceWorkflowStatus rws : entity.getWorkflowStatus()) {
				if ( (rws.getIsCurrent() == 1) && groupAbbrev.equals(rws.getGroupTerm().getAbbreviation()) ) {
					rws.setIsCurrent(0);
					break;				// no more can match, so exit the loop
				}
			}
		}

		// add a new status record for this change -- and need to persist this new object to the
		// database explicitly, before the whole reference gets persisted later on.

		ReferenceWorkflowStatus newRws = new ReferenceWorkflowStatus();
		newRws.set_refs_key(entity.get_refs_key());
		newRws.setIsCurrent(1);
		//newRws.setGroutTerm(termDAO.get());
		newRws.setGroupTerm(getTermByAbbreviation(Constants.VOC_WORKFLOW_GROUP, groupAbbrev));
		//newRws.setStatusTerm(termDAO.get());
		newRws.setStatusTerm(getTermByTerm(Constants.VOC_WORKFLOW_STATUS, newStatus));
		newRws.setCreatedBy(user);
		newRws.setModifiedBy(user);
		newRws.setCreation_date(new Date());
		newRws.setModification_date(new Date());
		entity.getWorkflowStatus().add(newRws);

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
	private boolean applyWorkflowStatusChanges(LTReference entity, LTReferenceDomain domain, User user) throws NonFatalAPIException, APIException {
		// note that we need to put 'anyChanges' last for each OR pair, otherwise short-circuit evaluation
		// will only let the first change go through and the rest will not execute.

		boolean anyChanges = updateWorkflowStatus(entity, Constants.WG_AP, entity.getStatus(Constants.WG_AP), domain.ap_status, user);
		anyChanges = updateWorkflowStatus(entity, Constants.WG_GO, entity.getStatus(Constants.WG_GO), domain.go_status, user) || anyChanges;
		anyChanges = updateWorkflowStatus(entity, Constants.WG_GXD, entity.getStatus(Constants.WG_GXD), domain.gxd_status, user) || anyChanges;
		anyChanges = updateWorkflowStatus(entity, Constants.WG_PRO, entity.getStatus(Constants.WG_PRO), domain.pro_status, user) || anyChanges;
		anyChanges = updateWorkflowStatus(entity, Constants.WG_QTL, entity.getStatus(Constants.WG_QTL), domain.qtl_status, user) || anyChanges;
		anyChanges = updateWorkflowStatus(entity, Constants.WG_TUMOR, entity.getStatus(Constants.WG_TUMOR), domain.tumor_status, user) || anyChanges;

		if (anyChanges) {
			
			entity.clearWorkflowStatusCache();

			// if no J# and Status in (Chosen, INdexed, Full-coded), then add J#

			if (entity.getJnumid() == null) {

				boolean addJnumid = false;

				for (String workgroup : Constants.WG_ALL) {
					String wgStatus = entity.getStatus(workgroup);
					if ((wgStatus != null) && (
							wgStatus.equals(Constants.WS_CHOSEN) ||
							wgStatus.equals(Constants.WS_INDEXED) ||
							wgStatus.equals(Constants.WS_CURATED))) {
						addJnumid = true;
						break;
					}
				}

				if (addJnumid) {
					try {
						log.info("assigning new J: number");
						assignNewJnumID(String.valueOf(entity.get_refs_key()), user.get_user_key());
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
	private boolean applyAlleleAssocChanges(LTReference entity, List<MGIReferenceAlleleAssocDomain> domain, User user) {
		// referenceAssocService will handle add (c), delete (d)

		boolean anyChanges = false;

		if (domain != null) {
			if (referenceAssocService.processAlleleAssoc(domain, user)) {
				anyChanges = true;
			}
		}
		
		return anyChanges;
	}

	/* apply any changes from domain to entity for the reference/strain associations
	 */
	private boolean applyStrainAssocChanges(LTReference entity, List<MGIReferenceStrainAssocDomain> domain, User user) {
		// referenceAssocService will handle add (c), delete (d)

		boolean anyChanges = false;

		if (domain != null) {
			if (referenceAssocService.processStrainAssoc(domain, user)) {
				anyChanges = true;
			}
		}
		
		return anyChanges;
	}

	/* apply any changes from domain to entity for the reference/marker associations
	 */
	private boolean applyMarkerAssocChanges(LTReference entity, List<MGIReferenceMarkerAssocDomain> domain, User user) {
		// referenceAssocService will handle add (c), delete (d)

		boolean anyChanges = false;

		if (domain != null) {
			if (referenceAssocService.processMarkerAssoc(domain, user)) {
				anyChanges = true;
			}
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

	/* add a new J: number for the given reference key and user key
	 */
	private void assignNewJnumID(String refsKey, int userKey) throws Exception {
		log.info("select count(1) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		Query query = referenceDAO.createNativeQuery("select count(*) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		query.getResultList();	
		return;
	}

}
