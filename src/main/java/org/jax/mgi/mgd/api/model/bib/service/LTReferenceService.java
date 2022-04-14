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
import org.jax.mgi.mgd.api.model.bib.repository.LTReferenceRepository;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class LTReferenceService {

	@Inject
	private LTReferenceRepository repo;	
		
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

	public SearchResults<LTReferenceDomain> getReferences(Map<String, Object> searchFields) throws APIException {
		return repo.search(searchFields);
	}	
	
}
