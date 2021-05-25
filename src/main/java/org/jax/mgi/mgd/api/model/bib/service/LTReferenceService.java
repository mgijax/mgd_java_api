package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.exception.NonFatalAPIException;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.repository.LTReferenceRepository;
import org.jax.mgi.mgd.api.model.bib.repository.LTReferenceSummaryRepository;
import org.jax.mgi.mgd.api.model.bib.translator.LTReferenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class LTReferenceService {

	/* Using a repository rather than hitting the DAO directly, as the repo layer handles some of the complexity 
	 * of mapping from domains to entities.
	 */
	@Inject
	private LTReferenceRepository repo;
	
	@Inject
	private LTReferenceSummaryRepository summaryRepo;

	// for searchSQL
	@Inject
	private LTReferenceDAO ltrefDAO;	
	private LTReferenceTranslator translator = new LTReferenceTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	/* Update the reference entity corresponding to the given domain object (and updates citation cache).  Returns
	 * domain object if successful or throws APIException if not.
	 */
	@Transactional
	public LTReferenceDomain updateReference(LTReferenceDomain domain, User currentUser) throws FatalAPIException, NonFatalAPIException, APIException {
		Logger log = Logger.getLogger(getClass());
		log.info("in LTReferenceService");
		repo.update(domain, currentUser);
		log.info("back in LTReferenceService");
		return repo.get(domain.refsKey);
	}

	/* returns true if references were updated, false if not; does not update citation cache, as
	 * only workflow tags are processed currently
	 */
	@Transactional
	public void updateReferencesInBulk(List<String> refsKey, String workflow_tag, String workflow_tag_operation, User currentUser) throws APIException {
		repo.updateInBulk(refsKey, workflow_tag, workflow_tag_operation, currentUser);
	}

	public SearchResults<LTReferenceDomain> getReference(String refsKey) throws APIException {
		SearchResults<LTReferenceDomain> results = new SearchResults<LTReferenceDomain>();
		try {
			LTReferenceDomain domain = repo.get(refsKey);
			results.total_count = 1;
			results.items = new ArrayList<LTReferenceDomain>();
			results.items.add(domain); 
		} catch (APIException e) {
			results.setError("Failure", "Failed to retrieve reference with key " + refsKey + ": " + e.toString(), Constants.HTTP_SERVER_ERROR);
		}
		return results;
	}
	
	public SearchResults<LTReferenceSummaryDomain> getReferenceSummaries(Map<String, Object> searchFields) throws APIException {
		return summaryRepo.search(searchFields);
	}

	public SearchResults<LTReferenceDomain> getReferences(Map<String, Object> searchFields) throws APIException {
		return repo.search(searchFields);
	}

	@Transactional	
	public SearchResults<LTReferenceDomain> searchSQL(LTReferenceDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		SearchResults<LTReferenceDomain> results = new SearchResults<LTReferenceDomain>();
		List<LTReferenceDomain> listOfResults = new ArrayList<LTReferenceDomain>();
		
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
		
		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("r", searchDomain.getCreated_by(), searchDomain.getModified_by(), searchDomain.getCreation_date(), searchDomain.getModification_date());
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
		List<String> accIds = new ArrayList<String>();
		if (searchDomain.getMgiid() != null) {
			accIds.add("'" + searchDomain.getMgiid().toUpperCase() + "'");
		}
		if (searchDomain.getJnumid() != null) {
			accIds.add("'" + searchDomain.getJnumid().toUpperCase() + "'");
		}	
		if (searchDomain.getPubmedid() != null) {
			accIds.add("'" + searchDomain.getPubmedid().toUpperCase() + "'");
		}
		if (searchDomain.getDoiid() != null) {
			accIds.add("'" + searchDomain.getDoiid().toUpperCase() + "'");
		}			
		if (accIds.size() > 0) {
			where = where + "\nand a.accID in (" + String.join(",",  accIds) + ")";
			from_accession = true;
		}

		// searchDomain.getAp_status = "xxxxx,xxxxx,xxxxx"
		String apWhere = "and (";
		String apClause = "exists (select 1 from BIB_Workflow_Status apWfs, VOC_Term apTerm"
						+ "\nwhere c._Refs_key = apwfs._Refs_key"
						+ "\nand apWfs._Group_key = 31576664"
						+ "\nand apWfs._Status_key = apTerm._Term_key"
						+ "\nand apTerm.term =";
		List<String> apStatus = new ArrayList<String>();
		if (searchDomain.getAp_status() != null && !searchDomain.getAp_status().isEmpty()) {
			apStatus.add("'" + searchDomain.getAp_status() + "'");
		}
		if (apStatus.size() > 0) {
			for (int i = 0; i < apStatus.size(); i++) {
				if (i > 0) {
					apWhere = apWhere + "\nand " + apClause + "'" + apStatus.get(i) + "'";
				}
				else {
					apWhere = apWhere + apClause + "'" + apStatus.get(i) + "'";
				}
			}
			apWhere = apWhere + ")";
		}
									
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

		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		//log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				LTReferenceDomain domain = new LTReferenceDomain();
				domain = translator.translate(ltrefDAO.get(rs.getInt("_refs_key")));
				ltrefDAO.clear();
				listOfResults.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		results.setItems(listOfResults);		
		return results;
	}	
	
	/* Get a list of valid relevance versions for use in a search pick list in the pwi.
	 */
	public List<String> getRelevanceVersions() {
		List<String> versions = new ArrayList<String>();

		String cmd = "select distinct version " + 
				"from bib_workflow_relevance " + 
				"where version is not null " +
				"order by version";
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				versions.add(rs.getString("version"));
			}
			sqlExecutor.cleanup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versions;
	}
}
