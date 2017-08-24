package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ReferenceService {

	@Inject
	private ReferenceDAO referenceDAO;
	
	public Reference createReference(Reference reference) {
		return referenceDAO.add(reference);
	}

	/* returns true if reference was updated, false if not; updates citation cache
	 */
	public boolean updateReference(ReferenceDomain reference, User currentUser) {
		try {
			referenceDAO.update(reference, currentUser);
			referenceDAO.updateCitationCache(reference._refs_key);
			return true;
		} catch (Throwable t) {
			return false;
		}

	}

	/* returns true if references were updated, false if not; does not update citation cache, as
	 * only workflow tags are processed currently
	 */
	public boolean updateReferencesInBulk(List<Long> refsKeys, String workflow_tag, String workflow_tag_operation, User currentUser) {
		try {
			referenceDAO.updateInBulk(refsKeys, workflow_tag, workflow_tag_operation, currentUser);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public SearchResults<Reference> getReference(HashMap<String, Object> searchFields) {
		return referenceDAO.search(searchFields);
	}

	public SearchResults<Reference> deleteReference(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(id != null) { map.put("primaryId", id); }
		SearchResults<Reference> results = referenceDAO.search(map);
		if (results.status_code != Constants.HTTP_OK) {
			return results;
		}
		return referenceDAO.delete(results.items.get(0));
	}

	public List<ReferenceWorkflowStatus> getStatusHistory(String refsKey) {
		return referenceDAO.getStatusHistory(refsKey);
	}
}
