package org.jax.mgi.mgd.api.model.bib.dao;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;

public class LTReferenceDAO extends PostgresSQLDAO<LTReference> {

	protected LTReferenceDAO() {
		super(LTReference.class);
	}

//	/* return a single reference for the given reference key with all needed lazy-loaded fields already loaded
//	 */
//	@Transactional
//	public LTReference getReference(String refsKey) throws APIException {
//		
//		if (refsKey == null || refsKey.isEmpty()) {return null;}
//		LTReference ref =  entityManager.find(LTReference.class, Integer.valueOf(refsKey));
//		if (ref == null) { return null; }
//		
//		Hibernate.initialize(ref.getWorkflowTag().size());
//		Hibernate.initialize(ref.getReferenceTypeTerm());
//		Hibernate.initialize(ref.getReferenceNote());
//		Hibernate.initialize(ref.getReferenceBook());
//		Hibernate.initialize(ref.getCreatedBy());
//		Hibernate.initialize(ref.getModifiedBy());
//		Hibernate.initialize(ref.getAssociatedData());
//		Hibernate.initialize(ref.getAccessionIDs());
//		Hibernate.initialize(ref.getWorkflowData());
//		Hibernate.initialize(ref.getWorkflowStatus());
//		return ref;
//	}
	
}
