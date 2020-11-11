package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceWorkflowDataDAO;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceBookDAO;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceNoteDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceCitationCacheTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.service.TermService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceService extends BaseService<ReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private ReferenceBookDAO bookDAO;
	@Inject
	private ReferenceNoteDAO noteDAO;
	@Inject
	private LTReferenceWorkflowDataDAO wfDataDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private AccessionService accessionService;
	@Inject
	private TermService termService;
	
	private ReferenceTranslator translator = new ReferenceTranslator();
	private ReferenceCitationCacheTranslator citationtranslator = new ReferenceCitationCacheTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ReferenceDomain> create(ReferenceDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		Reference entity = new Reference();
		
		// default reference type = ?
		if (domain.getReferenceTypeKey() == null || domain.getReferenceTypeKey().isEmpty()) {
			domain.setReferenceTypeKey("31576687");
		}
		entity.setReferenceType(termDAO.get(Integer.valueOf(domain.getReferenceTypeKey())));			

		if (domain.getAuthors() == null || domain.getAuthors().isEmpty()) {
			entity.setAuthors(null);
			entity.setPrimaryAuthor(null);
		}
		else {
			entity.setAuthors(domain.getAuthors());
			String[] authors = domain.getAuthors().split(";");
			entity.setPrimaryAuthor(authors[0]);
		}
		
		if (domain.getTitle() == null || domain.getTitle().isEmpty()) {
			entity.setTitle(null);
		}
		else {
			entity.setTitle(domain.getTitle());
		}
		
		if (domain.getJournal() == null || domain.getJournal().isEmpty()) {
			entity.setJournal(null);
		}
		else {
			entity.setJournal(domain.getJournal());
		}
		
		if (domain.getVol() == null || domain.getVol().isEmpty()) {
			entity.setVol(null);
		}
		else {
			entity.setVol(domain.getVol());
		}
		
		if (domain.getIssue() == null || domain.getIssue().isEmpty()) {
			entity.setIssue(null);
		}
		else {
			entity.setIssue(domain.getIssue());
		}
		
		if (domain.getPgs() == null || domain.getPgs().isEmpty()) {
			entity.setPgs(null);
		}
		else {
			entity.setPgs(domain.getPgs());
		}
				
		if (domain.getReferenceAbstract() == null || domain.getReferenceAbstract().isEmpty()) {
			entity.setReferenceAbstract(null);
		}
		else {
			entity.setReferenceAbstract(DecodeString.setDecodeToLatin9(domain.getReferenceAbstract()));
		}
		
		int theYear;
		if (domain.getYear() == null || domain.getYear().isEmpty()) {
			theYear = Calendar.getInstance().get(Calendar.YEAR);
		}
		else {
			theYear = Integer.valueOf(domain.getYear());
		}
		entity.setYear(theYear);
		
		if (domain.getDate() == null || domain.getDate().isEmpty()) {
			entity.setDate(String.valueOf(theYear));
		}
		else {
			entity.setDate(domain.getDate());
		}
		
		if (domain.getIsReviewArticle().equals("No")) {
			entity.setIsReviewArticle(0);
		}
		else {
			entity.setIsReviewArticle(1);
		}
		
//		if (domain.getIsDiscard().equals("No")) {
//			entity.setIsDiscard(0);
//		}
//		else {
//			entity.setIsDiscard(1);
//		}

		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		referenceDAO.persist(entity);
				
		// for books
		if (domain.getReferenceTypeKey().equals("31576679")) {
			ReferenceBook bookEntity = new ReferenceBook();
			bookEntity.set_refs_key(entity.get_refs_key());
			
			if (domain.book_author.isEmpty()) {
				bookEntity.setBook_author(null);
			}
			else {
				bookEntity.setBook_author(domain.book_author);
			}
	
			if (domain.book_author.isEmpty()) {
				bookEntity.setBook_title(null);
			}
			else {
				bookEntity.setBook_title(domain.book_title);
			}
			
			if (domain.book_author.isEmpty()) {
				bookEntity.setPlace(null);
			}
			else {
				bookEntity.setPlace(domain.place);
			}
			
			if (domain.book_author.isEmpty()) {
				bookEntity.setPublisher(null);
			}
			else {
				bookEntity.setPublisher(domain.publisher);
			}
									
			if (domain.book_author.isEmpty()) {
				bookEntity.setSeries_ed(null);
			}
			else {
				bookEntity.setSeries_ed(domain.series_ed);
			}
			
			bookEntity.setCreation_date(new Date());
			bookEntity.setModification_date(new Date());
			bookDAO.persist(bookEntity);
		}

		// notes
		if (domain.getReferenceNote() != null && !domain.getReferenceNote().isEmpty()) {
			ReferenceNote noteEntity = new ReferenceNote();
			noteEntity.set_refs_key(entity.get_refs_key());
			noteEntity.setNote(domain.referenceNote);			
			noteEntity.setCreation_date(new Date());
			noteEntity.setModification_date(new Date());
			noteDAO.persist(noteEntity);			
		}
				
		// supplement = Not checked (31576677)
		LTReferenceWorkflowData wfDataEntity = new LTReferenceWorkflowData();
		wfDataEntity.set_refs_key(entity.get_refs_key());
		wfDataEntity.setSupplementalTerm(termDAO.get(31576677));
		wfDataEntity.setExtractedTextTerm(termDAO.get(48804490));			
		wfDataEntity.setExtracted_text(null);
		wfDataEntity.setHas_pdf(0);
		wfDataEntity.setLink_supplemental(null);
		wfDataEntity.setCreatedByUser(user);
		wfDataEntity.setCreation_date(new Date());
		wfDataEntity.setModifiedByUser(user);
		wfDataEntity.setModification_date(new Date());
		wfDataDAO.persist(wfDataEntity);			
		
		// process pubmed accession ids
		if (domain.getPubmedid() != null && !domain.getPubmedid().isEmpty()) {
			AccessionDomain accessionDomain = new AccessionDomain();
			List<AccessionDomain> aresults = new ArrayList<AccessionDomain>();
			accessionDomain.setProcessStatus("c");
			accessionDomain.setAccID(domain.getPubmedid());
			accessionDomain.setLogicaldbKey("29");
			aresults.add(accessionDomain);
			accessionService.process(String.valueOf(entity.get_refs_key()), aresults, "Reference", user);
		}

		// process doiid accession ids
		if (domain.getDoiid() != null && !domain.getDoiid().isEmpty()) {
			AccessionDomain accessionDomain = new AccessionDomain();
			List<AccessionDomain> aresults = new ArrayList<AccessionDomain>();
			accessionDomain.setProcessStatus("c");
			accessionDomain.setAccID(domain.getDoiid());
			accessionDomain.setLogicaldbKey("65");
			aresults.add(accessionDomain);
			accessionService.process(String.valueOf(entity.get_refs_key()), aresults, "Reference", user);
		}
				
		// reload bib_citation_cache
		String cmd = "select count(*) from BIB_reloadCache (" + entity.get_refs_key() + ")";
		log.info("cmd: " + cmd);
		Query query = referenceDAO.createNativeQuery(cmd);
		query.getResultList();
		
		// return entity translated to domain
		log.info("processReference/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<ReferenceDomain> update(ReferenceDomain object, User user) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		Reference entity = referenceDAO.get(key);
		results.setItem(translator.translate(referenceDAO.get(key)));
		referenceDAO.remove(entity);
		return results;
	}

	@Transactional
	public ReferenceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain		
		ReferenceDomain domain = new ReferenceDomain();
		if (referenceDAO.get(key) != null) {
			domain = translator.translate(referenceDAO.get(key));
		}
		referenceDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results.setItem(translator.translate(referenceDAO.get(key)));
		referenceDAO.clear();
		return results;   	
    }

	@Transactional	
	public SearchResults<String> getJournalList() {
		// generate SQL command to return a list of distinct journals
		
		List<String> results = new ArrayList<String>();

		// building SQL command : select + from + where + orderBy
		String cmd = "select distinct journal from BIB_Refs where journal is not null";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.add(rs.getString("journal"));
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(results);
		return new SearchResults<String>(results);
	}	
	   
	@Transactional	
	public List<SlimReferenceDomain> search(ReferenceDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct c._refs_key, c.jnumid, c.numericPart, c.short_citation";
		String from = "from bib_citation_cache c, bib_refs r";
		String where = "where c._refs_key = r._refs_key";
		String 	orderBy = "order by c.short_citation";			
		String limit = Constants.SEARCH_RETURN_LIMIT;
		
		Boolean from_note = false;
		Boolean from_book = false;
		Boolean from_accession = false;
		Boolean from_editAccession = false;
		
		//Boolean from_allele = false;
		//Boolean from_marker = false;
		//Boolean from_strain = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("r", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getJnumid() != null && !searchDomain.getJnumid().isEmpty()) {
			String jnumid = searchDomain.getJnumid().toUpperCase();
			if (!jnumid.contains("J:")) {
				jnumid = "J:" + jnumid;
			}
			where = where + "\nand c.jnumid = '" + jnumid + "'";
		}
		
		if (searchDomain.getReferenceTypeKey() != null && !searchDomain.getReferenceTypeKey().isEmpty()) {
			where = where + "\nand r._ReferenceType_key = " + searchDomain.getReferenceTypeKey();
		}
		if (searchDomain.getAuthors() != null && !searchDomain.getAuthors().isEmpty()) {
			where = where + "\nand r.authors ilike '" + searchDomain.getAuthors() + "'";
		}
		if (searchDomain.getTitle() != null && !searchDomain.getTitle().isEmpty()) {
			where = where + "\nand r.title ilike '" + searchDomain.getTitle() + "'";
		}
		if (searchDomain.getJournal() != null && !searchDomain.getJournal().isEmpty()) {
			where = where + "\nand r.journal ilike '" + searchDomain.getJournal() + "'";
		}
		if (searchDomain.getVol() != null && !searchDomain.getVol().isEmpty()) {
			where = where + "\nand r.vol ilike '" + searchDomain.getVol() + "'";
		}
		if (searchDomain.getIssue() != null && !searchDomain.getIssue().isEmpty()) {
			where = where + "\nand r.issue ilike '" + searchDomain.getIssue() + "'";
		}
		if (searchDomain.getPgs() != null && !searchDomain.getPgs().isEmpty()) {
			where = where + "\nand r.pgs ilike '" + searchDomain.getPgs() + "'";
		}
		if (searchDomain.getDate() != null && !searchDomain.getDate().isEmpty()) {
			where = where + "\nand r.date ilike '" + searchDomain.getDate() + "'";
		}
		if (searchDomain.getYear() != null && !searchDomain.getYear().isEmpty()) {
			where = where + "\nand r.year = " + searchDomain.getYear();
		}		
		if (searchDomain.getIsReviewArticle() != null && !searchDomain.getIsReviewArticle().isEmpty()) {
			where = where + "\nand r.isReviewArticle = " + searchDomain.getIsReviewArticle();
		}
//		if (searchDomain.getIsDiscard() != null && !searchDomain.getIsDiscard().isEmpty()) {
//			where = where + "\nand r.isDiscard = " + searchDomain.getIsDiscard();
//		}
		if (searchDomain.getReferenceAbstract() != null && !searchDomain.getReferenceAbstract().isEmpty()) {
			where = where + "\nand r.abstract ilike '" + searchDomain.getReferenceAbstract() + "'";
		}
		
		// bib_books
		if (searchDomain.getBook_author() != null && !searchDomain.getBook_author().isEmpty()) {
			where = where + "\nand k.book_au ilike '" + searchDomain.getBook_author() + "'";
			from_book = true;
		}
		if (searchDomain.getBook_title() != null && !searchDomain.getBook_title().isEmpty()) {
			where = where + "\nand k.book_title ilike '" + searchDomain.getBook_title() + "'";
			from_book = true;
		}
		if (searchDomain.getPlace() != null && !searchDomain.getPlace().isEmpty()) {
			where = where + "\nand k.place ilike '" + searchDomain.getPlace() + "'";
			from_book = true;
		}
		if (searchDomain.getPublisher() != null && !searchDomain.getPublisher().isEmpty()) {
			where = where + "\nand k.publisher ilike '" + searchDomain.getPublisher() + "'";
			from_book = true;
		}
		if (searchDomain.getSeries_ed() != null && !searchDomain.getSeries_ed().isEmpty()) {
			where = where + "\nand k.series_ed ilike '" + searchDomain.getSeries_ed() + "'";
			from_book = true;
		}			
		
		// bib_notes
		if (searchDomain.getReferenceNote() != null && !searchDomain.getReferenceNote().isEmpty()) {
			where = where + "\nand n.note ilike '" + searchDomain.getReferenceNote() + "'";
			from_note = true;
		}
		
		// accession id
//		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().get(0).getAccID().isEmpty()) {
//			String mgiid = searchDomain.getMgiAccessionIds().get(0).getAccID().toUpperCase();
//			if (!mgiid.contains("MGI:")) {
//				mgiid = "MGI:" + mgiid;
//			}
//			where = where + "\nand a.accID ilike '" + mgiid + "'";
//			from_accession = true;
//		}
//		
//		// editable accession ids
//		if (searchDomain.getEditAccessionIds() != null) {
//			if (searchDomain.getEditAccessionIds().get(0).getAccID() != null 
//					&& !searchDomain.getEditAccessionIds().get(0).getAccID().isEmpty()) {
//				where = where + "\nand acc1.accID ilike '" +  searchDomain.getEditAccessionIds().get(0).getAccID() + "'";
//				from_editAccession = true;
//			}
//			if (searchDomain.getEditAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getEditAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
//				where = where + "\nand acc1._logicaldb_key = " + searchDomain.getEditAccessionIds().get(0).getLogicaldbKey();
//				from_editAccession = true;
//			}
//		}
										
		if (from_book == true) {
			from = from + ", bib_books k";
			where = where + "\nand c._refs_key = k._refs_key";
		}
		if (from_note == true) {
			from = from + ", bib_notes n";
			where = where + "\nand c._refs_key = n._refs_key";
		}
		if (from_accession == true) {
			from = from + ", bib_acc_view a";
			where = where + "\nand c._refs_key = a._object_key" 
					+ "\nand a._mgitype_key = 1";
		}
		if (from_editAccession == true) {
			from = from + ", bib_acc_view acc1";
			where = where + "\nand acc1._logicaldb_key in (29, 65, 185)" +
					"\nand c._refs_key = acc1._object_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimReferenceDomain domain = new SlimReferenceDomain();
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setJnumid(rs.getString("jnumid"));
				domain.setJnum(rs.getString("numericPart"));			
				domain.setShort_citation(rs.getString("short_citation"));			
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
		
	@Transactional	
	public List<ReferenceCitationCacheDomain> validJnum(String value) {
		// use SlimReferenceDomain to return list of validated reference
		// one value is expected
		// accepts value :  J:xxx or xxxx
		// returns empty list if value contains "%"
		// returns empty list if value does not exist

		List<ReferenceCitationCacheDomain> results = new ArrayList<ReferenceCitationCacheDomain>();
		
		if (value.contains("%") || value == null || value.isEmpty()) {
			return results;
		}

		String cmd = "\nselect _refs_key from bib_citation_cache";
		String where = "\nwhere ";
		
		value = value.toUpperCase();
		if (!value.contains("J:")) {
			value = "J:" + value;
		}
		where = where + "jnumid = '" + value + "'";

		cmd = cmd + where;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				ReferenceCitationCacheDomain domain = new ReferenceCitationCacheDomain();						
				domain = citationtranslator.translate(referenceDAO.get(rs.getInt("_refs_key")));			
				referenceDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@Transactional	
	public List<ReferenceCitationCacheDomain> validateJnumImage(SlimReferenceDomain domain) {
		// use ReferenceCitationCacheDomain to return list of validated reference
		// copyright
		// creative commons journal list

		List<ReferenceCitationCacheDomain> results = new ArrayList<ReferenceCitationCacheDomain>();

		// validate the jnum
		String jnum = "";
		
		log.info("reference/validateJnumImage/begin");
		
		if (domain.getJnum() != null && !domain.getJnum().isEmpty()) {
			jnum = domain.getJnum();
		}
		else if (domain.getJnumid() != null && !domain.getJnumid().isEmpty()) {
			jnum = domain.getJnumid();
		}
		
		results = validJnum(jnum);
	
		// if results is not null/empty
		// if reference key is not null/empty
		// if copyright is not null/empty
		// that is, do not overwrite an existing copyright
		
		if (results != null && !results.isEmpty()) {

			log.info("copyright check");
			// set copyright to incoming json package
			if (domain.getCopyright() != null && !domain.getCopyright().isEmpty()) {
				results.get(0).setCopyright(domain.getCopyright());
			}
			
			results.get(0).setNeedsDXDOIid(false);
			results.get(0).setIsCreativeCommons(false);
			
			String key = String.valueOf(results.get(0).get_refs_key());
			
			log.info("copyright validation");
			log.info(results.get(0).getCopyright());
				
			if (key != null && !key.isEmpty() 
					&& (results.get(0).getCopyright() == null
					|| results.get(0).getCopyright().isEmpty())) {
				

				log.info("termService.getJournalLicense():begin");				
				List<TermDomain> journalLicense = new ArrayList<TermDomain>();				
				journalLicense = termService.getJournalLicense(results.get(0).getJournal());
				log.info("termService.getJournalLicense():end");
				
				if (journalLicense.size() == 1) {

					log.info("validateJnumImage:found 1 item");
					String license = journalLicense.get(0).getAbbreviation();
					String copyright = license.replaceFirst("\\*", results.get(0).getShort_citation());
					log.info("validateJnumImage/copyright: " + copyright);

					// Proc Natl Acad Sci U S A
					// replace 1st * = short_citation
					// replace 2nd * = year
					if (results.get(0).getJournal().equals("Proc Natl Acad Sci U S A")) {
						copyright = copyright.replaceFirst("\\*", results.get(0).getYear());
					}

					// J Biol Chem
					// replace 1st * = short_citation
					// replace JBiolChem(||) = JbiolChem(pubmedid|JBC|)
					if (results.get(0).getJournal().equals("J Biol Chem")) {
						copyright = copyright.replaceFirst("JBioChem(||)", "JBioChem(" + results.get(0).getPubmedid() + "|JBC|)");
					}
					
					results.get(0).setCopyright(copyright);
					
					// if DXDOI is missing....
					if (journalLicense.get(0).getAbbreviation().contains("DXDOI(||)")) {
							results.get(0).setNeedsDXDOIid(true);
					}
					
					// does license contain 'Creative Commons'
					if (journalLicense.get(0).getAbbreviation().contains("Creative Commons")) {
						results.get(0).setIsCreativeCommons(true);							
					}
					
				}

//				String cmd = "\nselect * from bib_getCopyright(" + key + ")";
//				log.info("cmd: " + cmd);
//	
//				try {
//					Query query = referenceDAO.createNativeQuery(cmd);
//					String r = (String) query.getSingleResult();
//					results.get(0).setCopyright(r);
//						
//					// if DXDOI is missing....
//					if (r != null && !r.isEmpty()) {
//						if (r.contains("DXDOI(||)")) {
//							results.get(0).setNeedsDXDOIid(true);
//						}
//					}
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		}

		
		log.info("reference/validateJnumImage/end");
		return results;
	}
	
}
