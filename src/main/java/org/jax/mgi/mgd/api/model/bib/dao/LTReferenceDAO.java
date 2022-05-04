package org.jax.mgi.mgd.api.model.bib.dao;

import java.math.BigInteger;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jboss.logging.Logger;

public class LTReferenceDAO extends PostgresSQLDAO<LTReference> {

	protected LTReferenceDAO() {
		super(LTReference.class);
	}

	private Logger log = Logger.getLogger(getClass());

//	/* get a list of the workflow status records for a reference
//	 */
//	public List<LTReferenceWorkflowStatus> getStatusHistory (String refsKey) {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<LTReferenceWorkflowStatus> query = builder.createQuery(LTReferenceWorkflowStatus.class);
//		Root<LTReferenceWorkflowStatus> root = query.from(LTReferenceWorkflowStatus.class);
//
//		query.where(builder.equal(root.get("_refs_key"), refsKey));
//		query.orderBy(builder.desc(root.get("modification_date")));
//
//		return entityManager.createQuery(query).getResultList();
//	}

//	/* get a list of the workflow relevance records for a reference
//	 */
//	public List<LTReferenceWorkflowRelevance> getRelevanceHistory (String refsKey) {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<LTReferenceWorkflowRelevance> query = builder.createQuery(LTReferenceWorkflowRelevance.class);
//		Root<LTReferenceWorkflowRelevance> root = query.from(LTReferenceWorkflowRelevance.class);
//
//		query.where(builder.equal(root.get("_refs_key"), refsKey));
//		query.orderBy(builder.desc(root.get("modification_date")));
//
//		return entityManager.createQuery(query).getResultList();
//	}

	/* return a single reference for the given reference key with all needed lazy-loaded fields already loaded
	 */
	@Transactional
	public LTReference getReference(String refsKey) throws APIException {
		
		if (refsKey == null || refsKey.isEmpty()) {return null;}
		LTReference ref =  entityManager.find(LTReference.class, Integer.valueOf(refsKey));
		if (ref == null) { return null; }
		
		Hibernate.initialize(ref.getWorkflowTags().size());
		Hibernate.initialize(ref.getReferenceTypeTerm());
		Hibernate.initialize(ref.getReferenceNote());
		Hibernate.initialize(ref.getReferenceBook());
		Hibernate.initialize(ref.getCreatedBy());
		Hibernate.initialize(ref.getModifiedBy());
		Hibernate.initialize(ref.getAssociatedData());
		Hibernate.initialize(ref.getAccessionIDs());
		Hibernate.initialize(ref.getWorkflowData());
		Hibernate.initialize(ref.getWorkflowStatus());
		return ref;
	}

	/* get the next available primary key for a workflow tag record
	 */
	public synchronized int getNextWorkflowTagKey() {
		// returns an integer rather than *, as the void return was causing a mapping exception
		Query query = entityManager.createNativeQuery("select nextval('bib_workflow_tag_seq')");
		BigInteger results = (BigInteger) query.getSingleResult();
		return results.intValue();
	}

	/* add a new J: number for the given reference key and user key
	 */
	public void assignNewJnumID(String refsKey, int userKey) throws Exception {
		// returns an integer rather than *, as the void return was causing a mapping exception
		log.info("select count(1) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		Query query = entityManager.createNativeQuery("select count(*) from ACC_assignJ(" + userKey + "," + refsKey + ",-1)");
		query.getResultList();
		return;
	}
	
}
