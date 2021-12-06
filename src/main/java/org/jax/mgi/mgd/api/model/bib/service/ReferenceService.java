package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceWorkflowDataDAO;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceBookDAO;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceNoteDAO;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;
import org.jax.mgi.mgd.api.model.bib.translator.LTReferenceSummaryTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.SlimReferenceTranslator;
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
	private LTReferenceDAO ltReferenceDAO;
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
	private LTReferenceSummaryTranslator lttranslator = new LTReferenceSummaryTranslator();
	
	private SlimReferenceTranslator slimtranslator = new SlimReferenceTranslator();	
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
	public SearchResults<LTReferenceSummaryDomain> searchLT(Map<String, Object> params) {
		// 1st step in trying to merge LT search to Reference search
				
		// try to copy the input params to the ReferenceDomain
		ReferenceDomain searchDomain = new ReferenceDomain();
		
		if (params.containsKey("currentRelevance") && !params.get("currentRelevance").equals("Search All")) {
			searchDomain.setCurrentRelevance((String) params.get("currentRelevance"));
		}

		if (params.containsKey("workflow_tag_operator")) {
			searchDomain.setWorkflow_tag_operator((String) params.get("workflow_tag_operator"));
		}
		
		if (params.containsKey("status_operator")) {
			searchDomain.setStatus_operator((String) params.get("status_operator"));
		}
		
		if (params.containsKey("orderBy")) {
			searchDomain.setOrderBy((String) params.get("orderBy"));
		}
		
		if (params.containsKey("accids")) {
			searchDomain.setAccids((String) params.get("accids"));
		}
		
		if (params.containsKey("supplementalTerm")) {
			searchDomain.setSupplementalTerm((String) params.get("supplementalTerm"));
		}

		if (params.containsKey("_refs_key")) {
			searchDomain.setRefsKey((String) params.get("_refs_key"));
		}
		
		if (params.containsKey("created_by")) {
			searchDomain.setCreatedBy((String) params.get("created_by"));
		}
		
		if (params.containsKey("modified_by")) {
			searchDomain.setModifiedBy((String) params.get("modified_by"));
		}	
		
		if (params.containsKey("creation_date")) {
			searchDomain.setCreation_date((String) params.get("creation_date"));
		}
		
		if (params.containsKey("modification_date")) {
			searchDomain.setModification_date((String) params.get("modification_date"));	
		}		
		
		if (params.containsKey("referenceType")) {
			searchDomain.setReferenceType((String) params.get("referenceType"));
		}
		
		if (params.containsKey("primaryAuthor")) {
			searchDomain.setPrimaryAuthor((String) params.get("primaryAuthor"));
		}	
		
		if (params.containsKey("authors")) {
			searchDomain.setAuthors((String) params.get("authors"));
		}
		
		if (params.containsKey("title")) {
			searchDomain.setTitle((String) params.get("title"));
		}
		
		if (params.containsKey("journal")) {
			searchDomain.setJournal((String) params.get("journal"));
		}
		
		if (params.containsKey("vol")) {
			searchDomain.setVol((String) params.get("vol"));
		}
		
		if (params.containsKey("issue")) {
			searchDomain.setIssue((String) params.get("issue"));
		}
		
		if (params.containsKey("date")) {
			searchDomain.setDate((String) params.get("date"));
		}
		
		if (params.containsKey("year")) {
			searchDomain.setYear((String) params.get("year"));
		}
		
		if (params.containsKey("pgs")) {
			searchDomain.setPgs((String) params.get("pgs"));
		}
		
		if (params.containsKey("referenceAbstract")) {
			searchDomain.setReferenceAbstract((String) params.get("referenceAbstract"));
		}
		
		if (params.containsKey("isReviewArticle")) {
			if (params.get("isReviewArticle").equals("no")) {
				searchDomain.setIsReviewArticle("0");
			}
			else {
				searchDomain.setIsReviewArticle("1");				
			}
		}		
		
		if (params.containsKey("sh_status")) {
			searchDomain.setSh_status((String) params.get("sh_status"));
		}
		if (params.containsKey("sh_group")) {
			searchDomain.setSh_group((String) params.get("sh_group"));
		}
		if (params.containsKey("sh_username")) {
			searchDomain.setSh_username((String) params.get("sh_username"));
		}
		if (params.containsKey("sh_date")) {
			searchDomain.setSh_date((String) params.get("sh_date"));
		}	
		
		if (params.containsKey("relevance")) {
			searchDomain.setRelevance((String) params.get("relevance"));
		}
		if (params.containsKey("relevance_date")) {
			searchDomain.setRelevance_date((String) params.get("relevance_date"));
		}
		if (params.containsKey("relevance_user")) {
			searchDomain.setRelevance_user((String) params.get("relevance_user"));
		}
		if (params.containsKey("relevance_version")) {
			searchDomain.setRelevance_version((String) params.get("relevance_version"));
		}
		if (params.containsKey("relevance_confidence")) {
			searchDomain.setRelevance_confidence((String) params.get("relevance_confidence"));
		}
		
		if (params.containsKey("workflow_tag1")) {
			searchDomain.setWorkflow_tag1((String) params.get("workflow_tag1"));
		}
		if (params.containsKey("workflow_tag2")) {
			searchDomain.setWorkflow_tag2((String) params.get("workflow_tag2"));
		}
		if (params.containsKey("workflow_tag3")) {
			searchDomain.setWorkflow_tag3((String) params.get("workflow_tag3"));
		}
		if (params.containsKey("workflow_tag4")) {
			searchDomain.setWorkflow_tag4((String) params.get("workflow_tag4"));
		}
		if (params.containsKey("workflow_tag5")) {
			searchDomain.setWorkflow_tag5((String) params.get("workflow_tag5"));
		}
		if (params.containsKey("not_workflow_tag1")) {
			searchDomain.setNot_workflow_tag1((Boolean) params.get("not_workflow_tag1"));
		}
		if (params.containsKey("not_workflow_tag2")) {
			searchDomain.setNot_workflow_tag2((Boolean) params.get("not_workflow_tag2"));
		}
		if (params.containsKey("not_workflow_tag3")) {
			searchDomain.setNot_workflow_tag3((Boolean) params.get("not_workflow_tag3"));
		}
		if (params.containsKey("not_workflow_tag4")) {
			searchDomain.setNot_workflow_tag4((Boolean) params.get("not_workflow_tag4"));
		}
		if (params.containsKey("not_workflow_tag5")) {
			searchDomain.setNot_workflow_tag5((Boolean) params.get("not_workflow_tag5"));
		}
				
		if (params.containsKey("status_AP_New")) {
			searchDomain.setStatus_AP_New((Integer) params.get("status_AP_New"));
		}
		if (params.containsKey("status_AP_Not_Routed")) {
			searchDomain.setStatus_AP_Not_Routed((Integer) params.get("status_AP_Not_Routed"));
		}	
		if (params.containsKey("status_AP_Routed")) {
			searchDomain.setStatus_AP_Routed((Integer) params.get("status_AP_Routed"));
		}	
		if (params.containsKey("status_AP_Chosen")) {
			searchDomain.setStatus_AP_Chosen((Integer) params.get("status_AP_Chosen"));
		}	
		if (params.containsKey("status_AP_Indexed")) {
			searchDomain.setStatus_AP_Indexed((Integer) params.get("status_AP_Indexed"));
		}	
		if (params.containsKey("status_AP_Full_coded")) {
			searchDomain.setStatus_AP_Full_coded((Integer) params.get("status_AP_Full_coded"));
		}	
		if (params.containsKey("status_AP_Rejected")) {
			searchDomain.setStatus_AP_Rejected((Integer) params.get("status_AP_Rejected"));
		}			

		if (params.containsKey("status_GO_New")) {
			searchDomain.setStatus_GO_New((Integer) params.get("status_GO_New"));
		}
		if (params.containsKey("status_GO_Not_Routed")) {
			searchDomain.setStatus_GO_Not_Routed((Integer) params.get("status_GO_Not_Routed"));
		}	
		if (params.containsKey("status_GO_Routed")) {
			searchDomain.setStatus_GO_Routed((Integer) params.get("status_GO_Routed"));
		}	
		if (params.containsKey("status_GO_Chosen")) {
			searchDomain.setStatus_GO_Chosen((Integer) params.get("status_GO_Chosen"));
		}	
		if (params.containsKey("status_GO_Indexed")) {
			searchDomain.setStatus_GO_Indexed((Integer) params.get("status_GO_Indexed"));
		}	
		if (params.containsKey("status_GO_Full_coded")) {
			searchDomain.setStatus_GO_Full_coded((Integer) params.get("status_GO_Full_coded"));
		}	
		if (params.containsKey("status_GO_Rejected")) {
			searchDomain.setStatus_GO_Rejected((Integer) params.get("status_GO_Rejected"));
		}			
		
		if (params.containsKey("status_GXD_New")) {
			searchDomain.setStatus_GXD_New((Integer) params.get("status_GXD_New"));
		}
		if (params.containsKey("status_GXD_Not_Routed")) {
			searchDomain.setStatus_GXD_Not_Routed((Integer) params.get("status_GXD_Not_Routed"));
		}	
		if (params.containsKey("status_GXD_Routed")) {
			searchDomain.setStatus_GXD_Routed((Integer) params.get("status_GXD_Routed"));
		}	
		if (params.containsKey("status_GXD_Chosen")) {
			searchDomain.setStatus_GXD_Chosen((Integer) params.get("status_GXD_Chosen"));
		}	
		if (params.containsKey("status_GXD_Indexed")) {
			searchDomain.setStatus_GXD_Indexed((Integer) params.get("status_GXD_Indexed"));
		}	
		if (params.containsKey("status_GXD_Full_coded")) {
			searchDomain.setStatus_GXD_Full_coded((Integer) params.get("status_GXD_Full_coded"));
		}	
		if (params.containsKey("status_GXD_Rejected")) {
			searchDomain.setStatus_GXD_Rejected((Integer) params.get("status_GXD_Rejected"));
		}			
			
		if (params.containsKey("status_PRO_New")) {
			searchDomain.setStatus_PRO_New((Integer) params.get("status_PRO_New"));
		}
		if (params.containsKey("status_PRO_Not_Routed")) {
			searchDomain.setStatus_PRO_Not_Routed((Integer) params.get("status_PRO_Not_Routed"));
		}	
		if (params.containsKey("status_PRO_Routed")) {
			searchDomain.setStatus_PRO_Routed((Integer) params.get("status_PRO_Routed"));
		}	
		if (params.containsKey("status_PRO_Chosen")) {
			searchDomain.setStatus_PRO_Chosen((Integer) params.get("status_PRO_Chosen"));
		}	
		if (params.containsKey("status_PRO_Indexed")) {
			searchDomain.setStatus_PRO_Indexed((Integer) params.get("status_PRO_Indexed"));
		}	
		if (params.containsKey("status_PRO_Full_coded")) {
			searchDomain.setStatus_PRO_Full_coded((Integer) params.get("status_PRO_Full_coded"));
		}	
		if (params.containsKey("status_PRO_Rejected")) {
			searchDomain.setStatus_PRO_Rejected((Integer) params.get("status_PRO_Rejected"));
		}			
			
		if (params.containsKey("status_QTL_New")) {
			searchDomain.setStatus_QTL_New((Integer) params.get("status_QTL_New"));
		}
		if (params.containsKey("status_QTL_Not_Routed")) {
			searchDomain.setStatus_QTL_Not_Routed((Integer) params.get("status_QTL_Not_Routed"));
		}	
		if (params.containsKey("status_QTL_Routed")) {
			searchDomain.setStatus_QTL_Routed((Integer) params.get("status_QTL_Routed"));
		}	
		if (params.containsKey("status_QTL_Chosen")) {
			searchDomain.setStatus_QTL_Chosen((Integer) params.get("status_QTL_Chosen"));
		}	
		if (params.containsKey("status_QTL_Indexed")) {
			searchDomain.setStatus_QTL_Indexed((Integer) params.get("status_QTL_Indexed"));
		}	
		if (params.containsKey("status_QTL_Full_coded")) {
			searchDomain.setStatus_QTL_Full_coded((Integer) params.get("status_QTL_Full_coded"));
		}	
		if (params.containsKey("status_QTL_Rejected")) {
			searchDomain.setStatus_QTL_Rejected((Integer) params.get("status_QTL_Rejected"));
		}		
		
		if (params.containsKey("status_Tumor_New")) {
			searchDomain.setStatus_Tumor_New((Integer) params.get("status_Tumor_New"));
		}
		if (params.containsKey("status_Tumor_Not_Routed")) {
			searchDomain.setStatus_Tumor_Not_Routed((Integer) params.get("status_Tumor_Not_Routed"));
		}	
		if (params.containsKey("status_Tumor_Routed")) {
			searchDomain.setStatus_Tumor_Routed((Integer) params.get("status_Tumor_Routed"));
		}	
		if (params.containsKey("status_Tumor_Chosen")) {
			searchDomain.setStatus_Tumor_Chosen((Integer) params.get("status_Tumor_Chosen"));
		}	
		if (params.containsKey("status_Tumor_Indexed")) {
			searchDomain.setStatus_Tumor_Indexed((Integer) params.get("status_Tumor_Indexed"));
		}	
		if (params.containsKey("status_Tumor_Full_coded")) {
			searchDomain.setStatus_Tumor_Full_coded((Integer) params.get("status_Tumor_Full_coded"));
		}	
		if (params.containsKey("status_Tumor_Rejected")) {
			searchDomain.setStatus_Tumor_Rejected((Integer) params.get("status_Tumor_Rejected"));
		}

		// send this domain to the search()
		List<SlimReferenceDomain> returnDomain = new ArrayList<SlimReferenceDomain>();
		returnDomain = search(searchDomain);
		
		// return the LTReference results		
		SearchResults<LTReferenceSummaryDomain> summaryResults = new SearchResults<LTReferenceSummaryDomain>();
		summaryResults.items = new ArrayList<LTReferenceSummaryDomain>();

		// change this to take the refs key and translate the summary by the refs key not the entity
		for (int i = 0; i < returnDomain.size(); i++) {
			log.info("returnDomain.get(i).getRefsKey():" + returnDomain.get(i).getRefsKey());
			LTReference entity = ltReferenceDAO.get(Integer.valueOf(returnDomain.get(i).getRefsKey()));
			summaryResults.items.add(lttranslator.translate(entity));	
		}

//		results.elapsed_ms = refs.elapsed_ms;
//		results.error = refs.error;
//		results.message = refs.message;
//		results.status_code = refs.status_code;
		summaryResults.total_count = returnDomain.size();
//		results.all_match_count = refs.all_match_count;
		
		return summaryResults;		
	}
	
	@Transactional	
	public List<SlimReferenceDomain> search(ReferenceDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		
		String select = "select distinct c.*"
				+ "\n, apt.term as ap_status, got.term as go_status, gxdt.term as gxd_status"
				+ "\n, prot.term as pro_status, qtlt.term as qtl_status, tumort as tumor_status";
		
		String from = "from bib_citation_cache c, bib_refs r"
				+ "\n, bib_workflow_status ap, voc_term apt"
				+ "\n, bib_workflow_status go, voc_term got"
				+ "\n, bib_workflow_status gxd, voc_term gxdt"
				+ "\n, bib_workflow_status pro, voc_term prot"
				+ "\n, bib_workflow_status qtl, voc_term qtl"
				+ "\n, bib_workflow_status tumor, voc_term tumort";
		
		String where = "where c._refs_key = r._refs_key"
				+ "\nand c._refs_key = ap._refs_key and ap.isCurrent = 1 and ap._group_key = 31576664 and ap._status_key = apt._term_key"
				+ "\nand c._refs_key = go._refs_key and go.isCurrent = 1 and go._group_key = 31576666 and ap._status_key = apt._term_key"
				+ "\nand c._refs_key = gxd._refs_key and gxd.isCurrent = 1 and gxd._group_key = 31576665 and ap._status_key = apt._term_key"
				+ "\nand c._refs_key = pro._refs_key and pro.isCurrent = 1 and pro._group_key = 78678148 and ap._status_key = apt._term_key"
				+ "\nand c._refs_key = qtl._refs_key and qtl.isCurrent = 1 and qtl._group_key = 31576668 and ap._status_key = apt._term_key"	
				+ "\nand c._refs_key = tumor._refs_key and tumor.isCurrent = 1 and tumor._group_key = 31576667 and ap._status_key = apt._term_key";
				
		String 	orderBy = "order by c.short_citation";			
		String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		String addToWhere = "";

		Boolean from_accession = false;
		Boolean from_note = false;
		Boolean from_book = false;
		Boolean from_mgiid = false;
		Boolean from_pubmedid = false;
		Boolean from_doiid = false;
		Boolean from_referenceType = false;
		Boolean from_wkfrelevance = false;
		Boolean from_wkfdata = false;
		Boolean from_wkfstatus = false;
		
		// may be a different order
		if (searchDomain.getOrderBy() != null && !searchDomain.getOrderBy().isEmpty()) {
			if (searchDomain.getOrderBy().equals("1")) {
				orderBy = "order by c.numericpart desc nulls last, c.mgiid asc";
			}
			else {
				orderBy = "order by c.mgiid";
			}
		}
		
		if (searchDomain.getAccids() != null && !searchDomain.getAccids().isEmpty()) {
			value = searchDomain.getAccids().trim().toLowerCase().replaceAll(" ",",").replaceAll(",", "','");
			where = where + "\nand lower(a.accid) in ('" + value + "')";
			from_accession = true;
		}
		
		// reference history
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
		else if (searchDomain.getReferenceType() != null && !searchDomain.getReferenceType().isEmpty()) {
			where = where + "\nand rtype.term = '" + searchDomain.getReferenceType() + "'";
			from_referenceType = true;
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
		if (searchDomain.getIsReviewArticle() != null && !searchDomain.getIsReviewArticle().isEmpty()) {
			where = where + "\nand r.isReviewArticle = " + searchDomain.getIsReviewArticle();
		}
		if (searchDomain.getReferenceAbstract() != null && !searchDomain.getReferenceAbstract().isEmpty()) {
			where = where + "\nand r.abstract ilike '" + searchDomain.getReferenceAbstract() + "'";
		}
		
		// add some logic to allow >=, <=, >, <, =, between xxx and zzz, or just the year
		if (searchDomain.getYear() != null && !searchDomain.getYear().isEmpty()) {
			value = searchDomain.getYear();
			if (value.startsWith("=")
					|| value.startsWith(">=")
					|| value.startsWith(">")
					|| value.startsWith("<=")
					|| value.startsWith("<")
					)
			{
				where = where + "\nand r.year " + value;
			}
			else if (value.contains("..")) {
				String[] tokens = value.split("\\.\\.");
				where = where + "\nand year >= " + tokens[0] + " and year <= " + tokens[1];
			}
			else {		
				where = where + "\nand r.year = " + value;
			}
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

		// mgiid accession ids
		if (searchDomain.getMgiid() != null) {
			if (!searchDomain.getMgiid().isEmpty()) {
				where = where + "\nand mid.accID ilike '" +  searchDomain.getMgiid() + "'";
				from_mgiid = true;
			}
		}		
		// doiid accession ids
		if (searchDomain.getDoiid() != null) {
			if (!searchDomain.getDoiid().isEmpty()) {
				where = where + "\nand did.accID ilike '" +  searchDomain.getDoiid() + "'";
				from_doiid = true;
			}
		}		
		// pubmed accession ids
		if (searchDomain.getPubmedid() != null) {
			if (!searchDomain.getPubmedid().isEmpty()) {
				where = where + "\nand pid.accID ilike '" +  searchDomain.getPubmedid() + "'";
				from_pubmedid = true;
			}
		}
	
		// supplemental term
		if (searchDomain.getSupplementalTerm() != null && !searchDomain.getSupplementalTerm().isEmpty()) {
			where = where + "\nand dt.term = '" + searchDomain.getSupplementalTerm() + "'";
			from_wkfdata = true;
		}
		
		// relevance history
		if (searchDomain.getCurrentRelevance() != null && !searchDomain.getCurrentRelevance().isEmpty()) {
			where = where + "\nand rt.term = '" + searchDomain.getCurrentRelevance() + "'";
			from_wkfrelevance = true;
		}
		else if (searchDomain.getRelevance() != null && !searchDomain.getRelevance().isEmpty()) {
			where = where + "\nand rt.term = '" + searchDomain.getRelevance() + "'";
			from_wkfrelevance = true;
		}
		if (searchDomain.getRelevance_version() != null && !searchDomain.getRelevance_version().isEmpty()) {
			where = where + "\nand wkfr.version = '" + searchDomain.getRelevance_version() + "'";
			from_wkfrelevance = true;
		}
		if (searchDomain.getRelevance_confidence() != null && !searchDomain.getRelevance_confidence().isEmpty()) {
			where = where + "\nand wkfr.confidence = " + searchDomain.getRelevance_confidence();
			from_wkfrelevance = true;
		}
		if ((searchDomain.getRelevance_user() != null && !searchDomain.getRelevance_user().isEmpty())
				|| (searchDomain.getRelevance_date() != null && !searchDomain.getRelevance_date().isEmpty())
			) {
			
			String cmResultsStatus[] = DateSQLQuery.queryByCreationModification("wkfr", null, searchDomain.getRelevance_user(), null, searchDomain.getRelevance_date());
			if (cmResultsStatus.length > 0) {
				cmResultsStatus[0] = cmResultsStatus[0].replaceAll("u1", "u3");
				cmResultsStatus[1] = cmResultsStatus[1].replaceAll("u1", "u3");
				cmResultsStatus[0] = cmResultsStatus[0].replaceAll("u2", "u4");
				cmResultsStatus[1] = cmResultsStatus[1].replaceAll("u2", "u4");				
				from = from + cmResultsStatus[0];
				where = where + cmResultsStatus[1];
				from_wkfrelevance = true;
			}
		}
		
		// status history
		if (searchDomain.getSh_status() != null && !searchDomain.getSh_status().isEmpty()) {
			where = where + "\nand st.term = '" + searchDomain.getSh_status() + "'";
			from_wkfstatus = true;
		}
		if (searchDomain.getSh_group() != null && !searchDomain.getSh_group().isEmpty()) {
			where = where + "\nand gt.term = '" + searchDomain.getSh_group() + "'";
			from_wkfstatus = true;
		}	
		if ((searchDomain.getSh_username() != null && !searchDomain.getSh_username().isEmpty())
				|| (searchDomain.getSh_date() != null && !searchDomain.getSh_date().isEmpty())
			) {
			String cmResultsStatus[] = DateSQLQuery.queryByCreationModification("wkfs", null, searchDomain.getSh_username(), null, searchDomain.getSh_date());
			if (cmResultsStatus.length > 0) {
				cmResultsStatus[0] = cmResultsStatus[0].replaceAll("u1", "u5");
				cmResultsStatus[1] = cmResultsStatus[1].replaceAll("u1", "u5");
				cmResultsStatus[0] = cmResultsStatus[0].replaceAll("u2", "u6");
				cmResultsStatus[1] = cmResultsStatus[1].replaceAll("u2", "u6");					
				from = from + cmResultsStatus[0];
				where = where + cmResultsStatus[1];
				from_wkfstatus = true;
			}
		}
			
		// process workflow tags
		if (searchDomain.getWorkflow_tag_operator() != null && !searchDomain.getWorkflow_tag_operator().isEmpty()) {
			
			String workflow_tag_operator = searchDomain.getWorkflow_tag_operator();
			String exists_operator = " exists";
			String select_operator = " (select 1 from bib_workflow_tag tg, voc_term tagt"
					+ " where r._refs_key = tg._refs_key and tg._tag_key = tagt._term_key and tagt.term = '";
			String useExists_operator = "";
			
			addToWhere = "";
			
			if (searchDomain.getWorkflow_tag1() != null && !searchDomain.getWorkflow_tag1().isEmpty()) {
				if (searchDomain.getNot_workflow_tag1() != null && searchDomain.getNot_workflow_tag1() == true) {
					useExists_operator = " not" + exists_operator;
				}
				else {
					useExists_operator = exists_operator;	
				}
				addToWhere = addToWhere + workflow_tag_operator + useExists_operator + select_operator + searchDomain.getWorkflow_tag1() + "')\n";
			}
			
			if (searchDomain.getWorkflow_tag2() != null && !searchDomain.getWorkflow_tag2().isEmpty()) {
				if (searchDomain.getNot_workflow_tag2() != null && searchDomain.getNot_workflow_tag2() == true) {
					useExists_operator = " not" + exists_operator;
				}
				else {
					useExists_operator = exists_operator;	
				}
				addToWhere = addToWhere + workflow_tag_operator + useExists_operator + select_operator + searchDomain.getWorkflow_tag2() + "')\n";
			}
			
			if (searchDomain.getWorkflow_tag3() != null && !searchDomain.getWorkflow_tag3().isEmpty()) {
				if (searchDomain.getNot_workflow_tag3() != null && searchDomain.getNot_workflow_tag3() == true) {
					useExists_operator = " not" + exists_operator;
				}
				else {
					useExists_operator = exists_operator;	
				}
				addToWhere = addToWhere + workflow_tag_operator + useExists_operator + select_operator + searchDomain.getWorkflow_tag3() + "')\n";
			}
			
			if (searchDomain.getWorkflow_tag4() != null && !searchDomain.getWorkflow_tag4().isEmpty()) {
				if (searchDomain.getNot_workflow_tag4() != null && searchDomain.getNot_workflow_tag4() == true) {
					useExists_operator = " not" + exists_operator;
				}
				else {
					useExists_operator = exists_operator;	
				}
				addToWhere = addToWhere + workflow_tag_operator + useExists_operator + select_operator + searchDomain.getWorkflow_tag4() + "')\n";
			}
			
			if (searchDomain.getWorkflow_tag5() != null && !searchDomain.getWorkflow_tag5().isEmpty()) {
				if (searchDomain.getNot_workflow_tag5() != null && searchDomain.getNot_workflow_tag5() == true) {
					useExists_operator = " not" + exists_operator;
				}
				else {
					useExists_operator = exists_operator;	
				}
				addToWhere = addToWhere + workflow_tag_operator + useExists_operator + select_operator + searchDomain.getWorkflow_tag5() + "')\n";
			}
			
			if (!addToWhere.isEmpty()) {
				addToWhere =  "\nand (" + addToWhere;
				addToWhere = addToWhere + "\n)";
				addToWhere = addToWhere.replaceAll("and \\(AND", "and(");
				addToWhere = addToWhere.replaceAll("and \\(OR", "and(");
				where = where + addToWhere;
			}
		}
		
		// process workflow status
		
		//	31576664 | Alleles & Phenotypes
		//	31576665 | Expression
		//	31576666 | Gene Ontology
		//	31576667 | Tumor
		//	31576668 | QTL
		//	78678148 | PRO
		//		
		//	31576671 | Chosen
		//	31576674 | Full-coded
		//	31576673 | Indexed
		//	71027551 | New
		//	31576669 | Not Routed
		//	31576672 | Rejected
		//	31576670 | Routed
		
		if (searchDomain.getStatus_operator() != null && !searchDomain.getStatus_operator().isEmpty()) {
		
			String status_operator = searchDomain.getStatus_operator();
			addToWhere = "";
			
			String statusWhereAP = status_operator + " exists (select 1 from bib_workflow_status ss where r._refs_key = ss._refs_key" +
						" and ss.isCurrent = 1 and ss._group_key = 31576664" + " and ss._status_key = ";
			String statusWhereGO = status_operator + " exists (select 1 from bib_workflow_status ss where r._refs_key = ss._refs_key" +
					" and ss.isCurrent = 1 and ss._group_key = 31576666" + " and ss._status_key = ";		
			String statusWhereGXD = status_operator + " exists (select 1 from bib_workflow_status ss where r._refs_key = ss._refs_key" +
					" and ss.isCurrent = 1 and ss._group_key = 31576665" + " and ss._status_key = ";
			String statusWherePRO = status_operator + " exists (select 1 from bib_workflow_status ss where r._refs_key = ss._refs_key" +
					" and ss.isCurrent = 1 and ss._group_key = 78678148" + " and ss._status_key = ";
			String statusWhereQTL = status_operator + " exists (select 1 from bib_workflow_status ss where r._refs_key = ss._refs_key" +
					" and ss.isCurrent = 1 and ss._group_key = 31576668" + " and ss._status_key = ";
			String statusWhereTumor = status_operator + " exists (select 1 from bib_workflow_status ss where r._refs_key = ss._refs_key" +
					" and ss.isCurrent = 1 and ss._group_key = 31576667" + " and ss._status_key = ";
						
			if (searchDomain.getStatus_AP_Chosen() != null && searchDomain.getStatus_AP_Chosen() == 1) {
				addToWhere = addToWhere + statusWhereAP + "31576671" + ")\n";
			}
			if (searchDomain.getStatus_AP_Full_coded() != null && searchDomain.getStatus_AP_Full_coded() == 1) {	
				addToWhere = addToWhere + statusWhereAP + "31576674" + ")\n";
			}
			if (searchDomain.getStatus_AP_Indexed() != null && searchDomain.getStatus_AP_Indexed() == 1) {	
				addToWhere = addToWhere + statusWhereAP + "31576673" + ")\n";			
			}
			if (searchDomain.getStatus_AP_New() != null && searchDomain.getStatus_AP_New() == 1) {	
				addToWhere = addToWhere + statusWhereAP + "71027551" + ")\n";	
			}		
			if (searchDomain.getStatus_AP_Not_Routed()!= null && searchDomain.getStatus_AP_Not_Routed() == 1) {
				addToWhere = addToWhere + statusWhereAP + "31576669" + ")\n";
			}
			if (searchDomain.getStatus_AP_Rejected() != null && searchDomain.getStatus_AP_Rejected() == 1) {	
				addToWhere = addToWhere + statusWhereAP + "31576672" + ")\n";
			}
			if (searchDomain.getStatus_AP_Routed() != null && searchDomain.getStatus_AP_Routed() == 1) {	
				addToWhere = addToWhere + statusWhereAP + "31576670" + ")\n";
			}

			if (searchDomain.getStatus_GO_Chosen() != null && searchDomain.getStatus_GO_Chosen() == 1) {
				addToWhere = addToWhere + statusWhereGO + "31576671" + ")\n";
			}
			if (searchDomain.getStatus_GO_Full_coded() != null && searchDomain.getStatus_GO_Full_coded() == 1) {	
				addToWhere = addToWhere + statusWhereGO + "31576674" + ")\n";
			}
			if (searchDomain.getStatus_GO_Indexed() != null && searchDomain.getStatus_GO_Indexed() == 1) {	
				addToWhere = addToWhere + statusWhereGO + "31576673" + ")\n";
			}
			if (searchDomain.getStatus_GO_New() != null && searchDomain.getStatus_GO_New() == 1) {	
				addToWhere = addToWhere + statusWhereGO + "71027551" + ")\n";
			}		
			if (searchDomain.getStatus_GO_Not_Routed()!= null && searchDomain.getStatus_GO_Not_Routed() == 1) {
				addToWhere = addToWhere + statusWhereGO + "31576669" + ")\n";
			}
			if (searchDomain.getStatus_GO_Rejected() != null && searchDomain.getStatus_GO_Rejected() == 1) {	
				addToWhere = addToWhere + statusWhereGO + "31576672" + ")\n";
			}
			if (searchDomain.getStatus_GO_Routed() != null && searchDomain.getStatus_GO_Routed() == 1) {	
				addToWhere = addToWhere + statusWhereGO + "31576670" + ")\n";
			}

			if (searchDomain.getStatus_GXD_Chosen() != null && searchDomain.getStatus_GXD_Chosen() == 1) {
				addToWhere = addToWhere + statusWhereGXD + "31576671" + ")\n";
			}
			if (searchDomain.getStatus_GXD_Full_coded() != null && searchDomain.getStatus_GXD_Full_coded() == 1) {	
				addToWhere = addToWhere + statusWhereGXD + "31576674" + ")\n";
			}
			if (searchDomain.getStatus_GXD_Indexed() != null && searchDomain.getStatus_GXD_Indexed() == 1) {	
				addToWhere = addToWhere + statusWhereGXD + "31576673" + ")\n";
			}
			if (searchDomain.getStatus_GXD_New() != null && searchDomain.getStatus_GXD_New() == 1) {	
				addToWhere = addToWhere + statusWhereGXD + "71027551" + ")\n";
			}		
			if (searchDomain.getStatus_GXD_Not_Routed()!= null && searchDomain.getStatus_GXD_Not_Routed() == 1) {
				addToWhere = addToWhere + statusWhereGXD + "31576669" + ")\n";
			}
			if (searchDomain.getStatus_GXD_Rejected() != null && searchDomain.getStatus_GXD_Rejected() == 1) {	
				addToWhere = addToWhere + statusWhereGXD + "31576672" + ")\n";
			}
			if (searchDomain.getStatus_GXD_Routed() != null && searchDomain.getStatus_GXD_Routed() == 1) {	
				addToWhere = addToWhere + statusWhereGXD + "31576670" + ")\n";
			}
			
			if (searchDomain.getStatus_PRO_Chosen() != null && searchDomain.getStatus_PRO_Chosen() == 1) {
				addToWhere = addToWhere + statusWherePRO + "31576671" + ")\n";
			}
			if (searchDomain.getStatus_PRO_Full_coded() != null && searchDomain.getStatus_PRO_Full_coded() == 1) {	
				addToWhere = addToWhere + statusWherePRO + "31576674" + ")\n";
			}
			if (searchDomain.getStatus_PRO_Indexed() != null && searchDomain.getStatus_PRO_Indexed() == 1) {	
				addToWhere = addToWhere + statusWherePRO + "31576673" + ")\n";
			}
			if (searchDomain.getStatus_PRO_New() != null && searchDomain.getStatus_PRO_New() == 1) {	
				addToWhere = addToWhere + statusWherePRO + "71027551" + ")\n";
			}		
			if (searchDomain.getStatus_PRO_Not_Routed()!= null && searchDomain.getStatus_PRO_Not_Routed() == 1) {
				addToWhere = addToWhere + statusWherePRO + "31576669" + ")\n";
			}
			if (searchDomain.getStatus_PRO_Rejected() != null && searchDomain.getStatus_PRO_Rejected() == 1) {	
				addToWhere = addToWhere + statusWherePRO + "31576672" + ")\n";
			}
			if (searchDomain.getStatus_PRO_Routed() != null && searchDomain.getStatus_PRO_Routed() == 1) {	
				addToWhere = addToWhere + statusWherePRO + "31576670" + ")\n";
			}
			
			if (searchDomain.getStatus_QTL_Chosen() != null && searchDomain.getStatus_QTL_Chosen() == 1) {
				addToWhere = addToWhere + statusWhereQTL + "31576671" + ")\n";
			}
			if (searchDomain.getStatus_QTL_Full_coded() != null && searchDomain.getStatus_QTL_Full_coded() == 1) {	
				addToWhere = addToWhere + statusWhereQTL + "31576674" + ")\n";
			}
			if (searchDomain.getStatus_QTL_Indexed() != null && searchDomain.getStatus_QTL_Indexed() == 1) {	
				addToWhere = addToWhere + statusWhereQTL + "31576673" + ")\n";
			}
			if (searchDomain.getStatus_QTL_New() != null && searchDomain.getStatus_QTL_New() == 1) {	
				addToWhere = addToWhere + statusWhereQTL + "71027551" + ")\n";
			}		
			if (searchDomain.getStatus_QTL_Not_Routed()!= null && searchDomain.getStatus_QTL_Not_Routed() == 1) {
				addToWhere = addToWhere + statusWhereQTL + "31576669" + ")\n";
			}
			if (searchDomain.getStatus_QTL_Rejected() != null && searchDomain.getStatus_QTL_Rejected() == 1) {	
				addToWhere = addToWhere + statusWhereQTL + "31576672" + ")\n";
			}
			if (searchDomain.getStatus_QTL_Routed() != null && searchDomain.getStatus_QTL_Routed() == 1) {	
				addToWhere = addToWhere + statusWhereQTL + "31576670" + ")\n";
			}
			
			if (searchDomain.getStatus_Tumor_Chosen() != null && searchDomain.getStatus_Tumor_Chosen() == 1) {
				addToWhere = addToWhere + statusWhereTumor + "31576671" + ")\n";
			}
			if (searchDomain.getStatus_Tumor_Full_coded() != null && searchDomain.getStatus_Tumor_Full_coded() == 1) {	
				addToWhere = addToWhere + statusWhereTumor + "31576674" + ")\n";
			}
			if (searchDomain.getStatus_Tumor_Indexed() != null && searchDomain.getStatus_Tumor_Indexed() == 1) {	
				addToWhere = addToWhere + statusWhereTumor + "31576673" + ")\n";
			}
			if (searchDomain.getStatus_Tumor_New() != null && searchDomain.getStatus_Tumor_New() == 1) {	
				addToWhere = addToWhere + statusWhereTumor + "71027551" + ")\n";
			}		
			if (searchDomain.getStatus_Tumor_Not_Routed()!= null && searchDomain.getStatus_Tumor_Not_Routed() == 1) {
				addToWhere = addToWhere + statusWhereTumor + "31576669" + ")\n";
			}
			if (searchDomain.getStatus_Tumor_Rejected() != null && searchDomain.getStatus_Tumor_Rejected() == 1) {	
				addToWhere = addToWhere + statusWhereTumor + "31576672" + ")\n";
			}
			if (searchDomain.getStatus_Tumor_Routed() != null && searchDomain.getStatus_Tumor_Routed() == 1) {	
				addToWhere = addToWhere + statusWhereTumor + "31576670" + ")\n";
			}
			
			if (!addToWhere.isEmpty()) {
				addToWhere =  "\nand (" + addToWhere;
				addToWhere = addToWhere + "\n)";
				addToWhere = addToWhere.replaceAll("and \\(AND", "and(");
				addToWhere = addToWhere.replaceAll("and \\(OR", "and(");
				where = where + addToWhere;
			}
		}
		 		
		if (from_accession == true) {
			from = from + ", acc_accession a";
			where = where + "\nand c._refs_key = a._object_key and a._mgitype_key = 1";
		}
		if (from_book == true) {
			from = from + ", bib_books k";
			where = where + "\nand c._refs_key = k._refs_key";
		}
		if (from_note == true) {
			from = from + ", bib_notes n";
			where = where + "\nand c._refs_key = n._refs_key";
		}
		if (from_mgiid == true) {
			from = from + ", bib_acc_view mid";
			where = where + "\nand c._refs_key = mid._object_key" 
					+ "\nand mid._mgitype_key = 1"
					+ "\nand mid._logicaldb_key = 1";
		}
		if (from_doiid == true) {
			from = from + ", bib_acc_view did";
			where = where + "\nand c._refs_key = did._object_key" 
					+ "\nand did._mgitype_key = 1" 
					+ "\nand did._logicaldb_key = 65";
		}		
		if (from_pubmedid == true) {
			from = from + ", bib_acc_view pid";
			where = where + "\nand c._refs_key = pid._object_key" 
					+ "\nand pid._mgitype_key = 1"
					+ "\nand pid._logicaldb_key = 29";
		}
		if (from_referenceType == true) {
			from = from + ", voc_term rtype";
			where = where + "\nand r._referencetype_key = rtype._term_key"
					+ "\nand rtype._vocab_key = 131";			
		}
		if (from_wkfdata == true) {
			from = from + ", bib_workflow_data wkfd, voc_term dt";
			where = where + "\nand c._refs_key = wkfd._refs_key"
					+ "\nand wkfd._extractedtext_key = 48804490"
					+ "\nand wkfd._supplemental_key = dt._term_key"
					+ "\nand dt._vocab_key = 130";
		}		
		if (from_wkfrelevance == true) {
			from = from + ", bib_workflow_relevance wkfr, voc_term rt";
			where = where + "\nand c._refs_key = wkfr._refs_key"
					+ "\nand wkfr.isCurrent = 1"
					+ "\nand wkfr._relevance_key = rt._term_key"
					+ "\nand rt._vocab_key = 149";
		}
		if (from_wkfstatus == true) {
			from = from + ", bib_workflow_status wkfs, voc_term st, voc_term gt";
			where = where + "\nand c._refs_key = wkfs._refs_key"
					+ "\nand wkfs.isCurrent = 1"
					+ "\nand wkfs._status_key = st._term_key"
					+ "\nand st._vocab_key = 128"
					+ "\nand wkfs._group_key = gt._term_key"
					+ "\nand gt._vocab_key = 127";
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
				domain.setJournal(rs.getString("journal"));
				domain.setMgiid(rs.getString("mgiid"));							
				domain.setDoiid(rs.getString("doiid"));				
				domain.setPubmedid(rs.getString("pubmedid"));
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
	public List<SlimReferenceDomain> validJnum(String value) {
		// use SlimReferenceDomain to return list of validated reference
		// one value is expected
		// accepts value :  J:xxx or xxxx
		// returns empty list if value contains "%"
		// returns empty list if value does not exist

		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();
		
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
				SlimReferenceDomain domain = new SlimReferenceDomain();						
				domain = slimtranslator.translate(referenceDAO.get(rs.getInt("_refs_key")));			
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
	public List<SlimReferenceDomain> validateJnumImage(SlimReferenceDomain domain) {
		// use ReferenceCitationCacheDomain to return list of validated reference
		// copyright
		// creative commons journal list

		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();

		// validate the jnum
		String jnum = "";
		
		log.info("validateJnumImage/begin");
		
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

			// set copyright to incoming json package
			if (domain.getCopyright() != null && !domain.getCopyright().isEmpty()) {
				results.get(0).setCopyright(domain.getCopyright());
			}
			
			results.get(0).setNeedsDXDOIid(false);
			
			String key = results.get(0).getRefsKey();
			String sqlPattern = "SQL\\((.*?)\\)";
				
			if (key != null && !key.isEmpty() 
					&& (results.get(0).getCopyright() == null
					|| results.get(0).getCopyright().isEmpty())) {
	
				// if incoming SlimReferenceDomain.selectedLicense exists, then use that License
				// else, find all Licenses for given Journal
				List<TermDomain> journalLicenses = new ArrayList<TermDomain>();
				if (domain.getSelectedJournalLicense() != null && !domain.getSelectedJournalLicense().isEmpty()) {
					journalLicenses.add(termService.get(Integer.valueOf(domain.getSelectedJournalLicense())));
				}
				else {
					journalLicenses = termService.getJournalLicense(results.get(0).getJournal());
					results.get(0).setJournalLicenses(journalLicenses);
				}

				//
				// compare results.get(0).getYear() with sql in journal/term/note (definition)
				// term=BMC Biochem, note=SQL(<2008)
				// J:xxxx, year = 2007 -> pass = true, year = 2008 -> pass = false
				//
				//		SQL(<2008)
				//		SQL(<=2008)
				//		SQL(>2008)
				//		SQL(>=2008)
				//		SQL(=2008)
				//		SQL(between 2003 and 2008)
				//
				
				for (int i = 0; i < journalLicenses.size(); i++) {
					if (journalLicenses.get(i).getNote() != null && !journalLicenses.get(i).getNote().isEmpty()) {
						if (journalLicenses.get(i).getNote().contains("SQL(")) {
							
							String sql = journalLicenses.get(i).getNote();
							String sqlYear = "";

							Integer intSqlYear = 0;
							Integer refYear = Integer.valueOf(results.get(0).getYear());
							
							Boolean checkLess = false;
							Boolean checkGreater = false;
							Boolean checkEqual = false;
							Boolean passYear = false;

							Pattern p;
							p = Pattern.compile(sqlPattern);
							Matcher m = p.matcher(sql);
							
							while (m.find()) {
								
								// example:  SQL(<2008)
								sqlYear = m.group(1);

								// remove "<", "<=", ">", ">=", "="
								// convert to Integer
								
								if (sqlYear.startsWith("<=") == true) {
									// SQL(<=2008)
									intSqlYear = Integer.valueOf(sqlYear.replace("<=", ""));
									checkLess = true;
									checkEqual = true;
								}
								else if (sqlYear.startsWith("<") == true) {
									// SQL(<2008)
									intSqlYear = Integer.valueOf(sqlYear.replace("<", ""));
									checkLess = true;
								}
								else if (sqlYear.startsWith(">=") == true) {
									// SQL(>=2008)
									intSqlYear = Integer.valueOf(sqlYear.replace(">=", ""));
									checkGreater = true;
									checkEqual = true;
								}
								else if (sqlYear.startsWith(">") == true) {
									// SQL(>2008)
									intSqlYear = Integer.valueOf(sqlYear.replace(">", ""));
									checkGreater = true;
								}
								else if (sqlYear.startsWith("=") == true) {
									// SQL(=2008)
									intSqlYear = Integer.valueOf(sqlYear.replace("=", ""));
									checkEqual = true;
								}
								else if (sqlYear.startsWith("between") == true) {
									// SQL(between 2003 and 2008)
									passYear = true;
									sqlYear = sqlYear.replace("between ", "");
									sqlYear = sqlYear.replace("and ",  "");
									// 2003 2008
									String[] array = sqlYear.split(" ", -1);
									int retResult1 =  refYear.compareTo(Integer.valueOf(array[0]));
									int retResult2 =  refYear.compareTo(Integer.valueOf(array[1]));
									if (retResult1 < 0) {
										passYear = false;
									}
									if (retResult2 > 0) {
										passYear = false;
									}
								}
								
								// compare journal/SQL year with reference/year
								// if compare matches the expected journal/SQL, then pass = true
								int retResult =  refYear.compareTo(intSqlYear);
								if (retResult > 0) {
									if (checkGreater) {
										passYear = true;
									}
								}
								else if (retResult < 0) {
									if (checkLess) {
										passYear = true;
									}
								}
								else if (checkEqual) {
									passYear = true;
								}
								
								log.info("validateJnumImage: " + "bib_refs.year:" + refYear + ",license.year:" + sqlYear + ",pass=" + passYear);
								
								// if passYear = true, then set journalLicenses = this license *only* 
								if (passYear) {
									List<TermDomain> newjournalLicenses = new ArrayList<TermDomain>();
									newjournalLicenses.add(journalLicenses.get(i));
									journalLicenses = newjournalLicenses;
									results.get(0).setJournalLicenses(journalLicenses);									
								}
						    }
						}
					}
				}
				
				if (journalLicenses.size() == 1) {

					String journal = results.get(0).getJournal();
					String license = journalLicenses.get(0).getAbbreviation();
					String copyright = license.replaceFirst("\\*", results.get(0).getShort_citation());
					
					log.info("validateJnumImage/journal/" + journal);
					
					// Proc Natl Acad Sci U S A
					// example: J:9
					// replace 1st * = short_citation
					// replace 2nd * = year
					if (journal.equals("Proc Natl Acad Sci U S A")) {
						//log.info("validateJnumImage/processing Proc Natl Acad Sci U S A");					
						copyright = copyright.replaceFirst("\\*", results.get(0).getYear());
					}

					// J Neurosci
					// example: J:xxx
					// replace 1st * = short_citation
					// replace 2nd * = year
					if (journal.equals("J Neurosci")) {
						log.info("validateJnumImage/processing J Neurosci w/year");					
						copyright = copyright.replaceFirst("\\*", results.get(0).getYear());
					}
					
					// J Biol Chem
					// example:  J:150
					// replace 1st * = short_citation
					// replace JBiolChem(||) = JBiolChem(pubmedid|JBC|)
					else if (journal.equals("J Biol Chem") && results.get(0).getPubmedid() != null) {
						//log.info("validateJnumImage/processing J Biol Chem");					
						copyright = copyright.replaceAll("JBiolChem\\(\\|\\|\\)", "JBiolChem(" + results.get(0).getPubmedid() + "|JBC|)");
					}
					
					// J Lipid Res
					// example:  J:75524
					// replace 1st * = short_citation
					// replace JLipidRes(||) = JLipidRes(pubmedid|JLR|)
					else if (journal.equals("J Lipid Res") && results.get(0).getPubmedid() != null) {
						//log.info("validateJnumImage/processing J Lipid Res");					
						copyright = copyright.replaceAll("JLipidRes\\(\\|\\|\\)", "JLipidRes(" + results.get(0).getPubmedid() + "|JLR|)");
					}
					
					// Elsevier
					// example: J:75717, J:15248
					// 
					else if (copyright.contains("Elsevier")) {
						//log.info("validateJnumImage/processing Elsevier");					
						if (results.get(0).getDoiid() != null) {
							copyright = copyright.replaceAll("DXDOI\\(\\|\\|\\)", "DXDOI(" + results.get(0).getDoiid() + "||)");					
						}
						copyright = copyright.replaceAll("Elsevier\\(\\|\\|\\)", "Elsevier(" + results.get(0).getJnumid() + "||)");					
					}

					// J Cell Biol
					// J Gen Physiol
					// J Expt Med
					// example: J:3145, J:2418, J:82858
					else if (results.get(0).getDoiid() != null) {
						copyright = copyright.replaceAll("DXDOI\\(\\|\\|\\)", "DXDOI(" + results.get(0).getDoiid() + "||)");
					}
					
					log.info("validateJnumImage/copyright/" + copyright);					
					results.get(0).setCopyright(copyright);
					
					// if DXDOI is missing....
					if (copyright.contains("DXDOI(||)")) {
							results.get(0).setNeedsDXDOIid(true);
					}					
				}
			}
		}
		
		log.info("validateJnumImage/end");
		return results;
	}
	
}
