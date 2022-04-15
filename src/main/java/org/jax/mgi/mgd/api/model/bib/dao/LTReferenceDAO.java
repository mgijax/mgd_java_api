package org.jax.mgi.mgd.api.model.bib.dao;

import java.math.BigInteger;
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
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowRelevance;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowStatus;
import org.jboss.logging.Logger;

public class LTReferenceDAO extends PostgresSQLDAO<LTReference> {

	protected LTReferenceDAO() {
		super(LTReference.class);
	}

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
		log.info("LTReferenceDAO:getReference(String refsKey):" + refsKey);
		log.info("getWorkflowTags()");
		Hibernate.initialize(ref.getWorkflowTags().size());
		log.info("getReferenceTypeTerm()");
		Hibernate.initialize(ref.getReferenceTypeTerm());
		log.info("getNotes()");
		Hibernate.initialize(ref.getNotes());
		log.info("getReferenceBook()");
		Hibernate.initialize(ref.getReferenceBook());
		log.info("getCreatedByUser()");
		Hibernate.initialize(ref.getCreatedByUser());
		log.info("getModifiedByUser()");
		Hibernate.initialize(ref.getModifiedByUser());
		log.info("getAssociatedData()");
		Hibernate.initialize(ref.getAssociatedData());
		log.info("getAccessionIDs()");
		Hibernate.initialize(ref.getAccessionIDs());
		log.info("getWorkflowData()");
		Hibernate.initialize(ref.getWorkflowData());
		log.info("getWorkflowStatuses()");
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
	
}
