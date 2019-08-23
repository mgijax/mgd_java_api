package org.jax.mgi.mgd.api.model.bib.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.exception.NonFatalAPIException;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.repository.LTReferenceRepository;
import org.jax.mgi.mgd.api.model.bib.repository.LTReferenceSummaryRepository;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class LTReferenceService {

	/* Using a repository rather than hitting the DAO directly, as the repo layer handles some of the complexity 
	 * of mapping from domains to entities.
	 */
	@Inject
	private LTReferenceRepository repo;
	
	@Inject
	private LTReferenceSummaryRepository summaryRepo;
	
	/* Create a reference entity that corresponds to the given domain object.  Returns domain object if
	 * successful or throws APIException if not.
	 */
	@Transactional
	public LTReferenceDomain createReference(LTReferenceDomain domain, User currentUser) throws APIException {
		return repo.create(domain, currentUser);
	}

	/* Update the reference entity corresponding to the given domain object (and updates citation cache).  Returns
	 * domain object if successful or throws APIException if not.
	 */
	@Transactional
	public LTReferenceDomain updateReference(LTReferenceDomain domain, User currentUser) throws FatalAPIException, NonFatalAPIException, APIException {
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

	public SearchResults<LTReferenceDomain> getReference(int refsKey) throws APIException {
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
	
}
