package org.jax.mgi.mgd.api.model.bib.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowRelevance;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class LTReferenceDAO extends PostgresSQLDAO<LTReference> {

	protected LTReferenceDAO() {
		super(LTReference.class);
	}

	private SQLExecutor sqlExecutor = new SQLExecutor();

	private Logger log = Logger.getLogger(getClass());

	/* get a list of the workflow status records for a reference
	 */
	public List<LTReferenceWorkflowStatus> getStatusHistory (String refsKey) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LTReferenceWorkflowStatus> query = builder.createQuery(LTReferenceWorkflowStatus.class);
		Root<LTReferenceWorkflowStatus> root = query.from(LTReferenceWorkflowStatus.class);

		query.where(builder.equal(root.get("_refs_key"), refsKey));
		query.orderBy(builder.desc(root.get("modification_date")));

		return entityManager.createQuery(query).getResultList();
	}

	/* get a list of the workflow relevance records for a reference
	 */
	public List<LTReferenceWorkflowRelevance> getRelevanceHistory (String refsKey) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LTReferenceWorkflowRelevance> query = builder.createQuery(LTReferenceWorkflowRelevance.class);
		Root<LTReferenceWorkflowRelevance> root = query.from(LTReferenceWorkflowRelevance.class);

		query.where(builder.equal(root.get("_refs_key"), refsKey));
		query.orderBy(builder.desc(root.get("modification_date")));

		return entityManager.createQuery(query).getResultList();
	}

	/* return a single reference for the given reference key with all needed lazy-loaded fields already loaded
	 */
	@Transactional
	public LTReference getReference(String refsKey) throws APIException {
		LTReference ref =  entityManager.find(LTReference.class, Integer.valueOf(refsKey));
		if (ref == null) { return null; }
		Hibernate.initialize(ref.getWorkflowTags().size());
		Hibernate.initialize(ref.getReferenceTypeTerm());
		Hibernate.initialize(ref.getNotes());
		Hibernate.initialize(ref.getReferenceBook());
		Hibernate.initialize(ref.getCreatedByUser());
		Hibernate.initialize(ref.getModifiedByUser());
		Hibernate.initialize(ref.getAssociatedData());
		Hibernate.initialize(ref.getAccessionIDs());
		Hibernate.initialize(ref.getWorkflowData());
		Hibernate.initialize(ref.getWorkflowStatuses());
		return ref;
	}

	/* get the next available primary key for a workflow status record
	 */
	public synchronized int getNextWorkflowStatusKey() throws FatalAPIException {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select nextval('bib_workflow_status_seq')");
		BigInteger results = (BigInteger) query.getSingleResult();
		return results.intValue();
	}

	/* get the next available primary key for a workflow tag record
	 */
	public synchronized int getNextWorkflowTagKey() {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select nextval('bib_workflow_tag_seq')");
		BigInteger results = (BigInteger) query.getSingleResult();
		return results.intValue();
	}

	/* get the next available primary key for a workflow relevance record
	 */
	public synchronized int getNextWorkflowRelevanceKey() {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select nextval('bib_workflow_relevance_seq')");
		BigInteger results = (BigInteger) query.getSingleResult();
		return results.intValue();
	}

	/* update the bib_citation_cache table for the given reference key
	 */
	public void updateCitationCache(String refsKey) {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select count(1) from BIB_reloadCache(" + refsKey + ")");
		query.getResultList();		
		return;
	}

	/* add a new J: number for the given reference key and user key
	 */
	public void assignNewJnumID(String refsKey, int userKey) throws Exception {
		// returns an integer rather than *, as the void return was causing a mapping exception
		log.info("select count(1) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		Query query = entityManager.createNativeQuery("select count(1) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		query.getResultList();
		return;
	}
	
	// Note: This method appears to be a no-op, as a query is generated and run, but no results
	// from it are collected.  (It will always return an empty set of search results.) -- jsb
	@Transactional	
	public SearchResults<LTReference> searchSQL(LTReferenceDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		SearchResults<LTReference> results = new SearchResults<LTReference>();

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
		if (searchDomain.getMgiid() != null && !searchDomain.getMgiid().isEmpty()) {
			String mgiid = searchDomain.getMgiid().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand a.accID ilike '" + mgiid + "'";
			from_accession = true;
		}
		
		// editable accession ids
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
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
