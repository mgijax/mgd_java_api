package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;

@RequestScoped
public class ReferenceService {

	@Inject
	private ReferenceDAO referenceDAO;
	
	public Reference createReference(Reference reference) {
		return referenceDAO.add(reference);
	}

	public Reference updateReference(Reference reference) {
		return referenceDAO.update(reference);
	}

	public List<Reference> getReference(HashMap<String, Object> searchFields) {
		return referenceDAO.get(searchFields);
	}

	public Reference deleteReference(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(id != null) { map.put("primaryId", id); }
		Reference reference = referenceDAO.get(map).get(0);
		return referenceDAO.delete(reference);
	}

	public List<ReferenceWorkflowStatus> getStatusHistory(String refsKey) {
		return referenceDAO.getStatusHistory(refsKey);
	}
}
