
package org.jax.mgi.mgd.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.domain.ReferenceSummaryDomain;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.exception.NonFatalAPIException;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.repository.ReferenceRepository;
import org.jax.mgi.mgd.api.repository.ReferenceSummaryRepository;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ReferenceService {

	/* Using a repository rather than hitting the DAO directly, as the repo layer handles some of the complexity 
	 * of mapping from domains to entities.
	 */
	@Inject
	private ReferenceRepository repo;

	@Inject
	private ReferenceSummaryRepository summaryRepo;
	
	/* Create a reference entity that corresponds to the given domain object.  Returns domain object if
	 * successful or throws APIException if not.
	 */
	@Transactional
	public ReferenceDomain createReference(ReferenceDomain domain, User currentUser) throws APIException {
		return repo.create(domain, currentUser);
	}

	/* Update the reference entity corresponding to the given domain object (and updates citation cache).  Returns
	 * domain object if successful or throws APIException if not.
	 */
	@Transactional
	public ReferenceDomain updateReference(ReferenceDomain domain, User currentUser) throws FatalAPIException, NonFatalAPIException, APIException {
		repo.update(domain, currentUser);
		return domain;
	}

	/* returns true if references were updated, false if not; does not update citation cache, as
	 * only workflow tags are processed currently
	 */
	@Transactional
	public void updateReferencesInBulk(List<Integer> refsKeys, String workflow_tag, String workflow_tag_operation, User currentUser) throws APIException {
		repo.updateInBulk(refsKeys, workflow_tag, workflow_tag_operation, currentUser);
	}

	public SearchResults<ReferenceDomain> getReference(int refsKey) throws APIException {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		try {
			ReferenceDomain domain = repo.get(refsKey);
			results.total_count = 1;
			results.items = new ArrayList<ReferenceDomain>();
			results.items.add(domain); 
		} catch (APIException e) {
			results.setError("Failure", "Failed to retrieve reference with key " + refsKey + ": " + e.toString(), Constants.HTTP_SERVER_ERROR);
		}
		return results;
	}
	
	public SearchResults<ReferenceSummaryDomain> getReferenceSummaries(Map<String, Object> searchFields) throws APIException {
		return summaryRepo.search(searchFields);
	}

	public SearchResults<ReferenceDomain> getReferences(Map<String, Object> searchFields) throws APIException {
		return repo.search(searchFields);
	}

	@Transactional
	public SearchResults<ReferenceDomain> deleteReference(String id, User currentUser) {
		SearchResults<ReferenceDomain> out = new SearchResults<ReferenceDomain>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(id != null) { map.put("primaryId", id); }
		SearchResults<ReferenceDomain> results = repo.search(map);
		if (results.status_code != Constants.HTTP_OK) {
			out.setError(results.error, results.message, results.status_code);
			return out;
		}

		out.setItem(results.items.get(0));
		try {
			repo.delete(results.items.get(0), currentUser);
		} catch (APIException e) {
			out.setError("Failed", "Failed to delete reference with ID " + id + ", exception: " + e.toString(), Constants.HTTP_SERVER_ERROR);
		}
		return out;
	}
}
