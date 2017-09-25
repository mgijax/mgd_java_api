package org.jax.mgi.mgd.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.domain.ReferenceBulkDomain;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.domain.ReferenceSummaryDomain;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.rest.interfaces.ReferenceRESTInterface;
import org.jax.mgi.mgd.api.service.ReferenceService;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.util.CommaSplitter;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.MapMaker;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class ReferenceController extends BaseController implements ReferenceRESTInterface {

	/***--- instance variables ---***/
	
	@Inject
	private ReferenceService referenceService;
	
	@Inject
	private TermService termService;
	
	@Inject
	private UserService userService;
	
	private Logger log = Logger.getLogger(getClass());

	/***--- methods ---***/
	
	/* create a database record for the given reference...  TODO: need to flesh this out, use SearchResults object, etc.
	 */
	@Override
	public SearchResults<ReferenceDomain> createReference(String api_access_token, String username, ReferenceDomain reference) {
		// Example request logging
		//logRequest("Reference API: createReference:", reference);
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		try {
			User currentUser = userService.getUser(username);
			if (currentUser != null) {
				results.setItem(referenceService.createReference(reference, currentUser));
			} 
		} catch (APIException e) {
			results.setError("ReferenceController.createReference", "Failed to create reference: " + e.toString(),
				Constants.HTTP_SERVER_ERROR);
		}
		return results;
	}

	/* update the given reference in the database, then return a revised version of it in the SearchResults
	 */
	@Override
	public SearchResults<ReferenceDomain> updateReference(String api_access_token, String username, ReferenceDomain reference) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();

		if (!authenticate(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User currentUser = userService.getUser(username);
		if (currentUser != null) {
			try {
				// The updateReference method does not return the updated reference, as the method must finish
				// before the updates are persisted to the database.  So, we issue the update, then we use the
				// getReferenceByKey() method to re-fetch and return the updated object.
				
				referenceService.updateReference(reference, currentUser);
				return this.getReferenceByKey(reference._refs_key.toString());
			} catch (Throwable t) {
				results.setError("Failed", "Failed to save changes (" + t.getMessage() + ")", Constants.HTTP_SERVER_ERROR);
			}
		} else {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED);
		}
		return results;
	}

	/* update the workflow group to be the given status for the given reference, taking care to keep the status history
	 * updated and to generate a J: number, if needed
	 */
	@Transactional
	@Override
	public SearchResults<String> updateReferenceStatus (String api_access_token, String username, String accid, String group, String status) {
		SearchResults<String> results = new SearchResults<String>();

		if (!authenticate(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		// check that we have a legitimate status value

		if (status == null) {
			results.setError("Failed", "Unknown status value: null", Constants.HTTP_BAD_REQUEST);
			return results;
		} else {
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("vocab.name", "Workflow Status");
			params.put("term", status);
			
			SearchResults<Term> terms = termService.search(params);
			if (terms.total_count == 0) {
				results.setError("Failed", "Unknown status term: " + status, Constants.HTTP_NOT_FOUND);
				return results;
			} else if (terms.total_count > 1) {
				results.setError("Failed", "Duplicate status terms: " + status, Constants.HTTP_BAD_REQUEST);
				return results;
			}
		}
		
		User currentUser = userService.getUser(username);
		if (currentUser == null) {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED); 
			return results;

		} else if (!currentUser.getLogin().equalsIgnoreCase(username)) {
				// We don't want to just fall back on a default user here, so this is an error case.
				results.setError("Failed", "Unknown user: " + username, Constants.HTTP_BAD_REQUEST);
				return results;
		}

		MapMaker mapMaker = new MapMaker();
		CommaSplitter splitter = new CommaSplitter();
		List<String> failures = new ArrayList<String>();
		String currentID = null;
		
		for (String myIDs : splitter.split(accid, 100)) {
			try {
				SearchResults<ReferenceDomain> refs = referenceService.getReferences(mapMaker.toMap("{\"accids\" : \"" + myIDs + "\"}"));

				if (refs.total_count > 0) {
					for (ReferenceDomain ref : refs.items) {
						currentID = ref.jnumid;
						ref.setStatus(group, status);
						referenceService.updateReference(ref, currentUser);
					}
				}
			} catch (APIException t) {
				failures.add(currentID);
			}
		}

		if (failures.size() > 0) {
			results.setError("Partial Failure", "Status changes failed to save for: " + String.join(",", failures), Constants.HTTP_SERVER_ERROR);
		} else {
			results.items = null;
		}

		return results;
	}
	
	/* add the specified tag to each of the references specified by key
	 */
	@Override
	public SearchResults<String> updateReferencesInBulk (String api_access_token, String username, ReferenceBulkDomain input) {
		SearchResults<String> results = new SearchResults<String>();

		if (!authenticate(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User currentUser = userService.getUser(username);
		if (currentUser != null) {
			try {
				referenceService.updateReferencesInBulk(input._refs_keys, input.workflow_tag, input.workflow_tag_operation, currentUser);
				results.items = null;	// okay result
			} catch (APIException t) {
				results.setError("Failed", "Failed to save changes", Constants.HTTP_SERVER_ERROR);
			}
		} else {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED);
		}
		return results;
	}
	
	/* search method - retrieves references based on query form parameters
	 */
	@Override
	public SearchResults<ReferenceSummaryDomain> search(Map<String,Object> params) {
		if (params.containsKey("isReviewArticle")) {
			String isReviewArticle = (String) params.get("isReviewArticle");
			if ("No".equalsIgnoreCase(isReviewArticle) || "0".equals(isReviewArticle)) {
				params.put("isReviewArticle", 0);
			} else if ("Yes".equalsIgnoreCase(isReviewArticle) || "1".equals(isReviewArticle)) {
				params.put("isReviewArticle", 1);
			}
		}

		params = filterEmptyParameters(params);
		log.info("Search Params: " + params);
		
		// brief error checking

		if (params.containsKey("year")) {
			try {
				// We don't need this value; we just need to ensure it's an integer.
				Integer.parseInt((String) params.get("year"));
			} catch (Throwable t) {
				SearchResults<ReferenceSummaryDomain> results = new SearchResults<ReferenceSummaryDomain>();
				results.setError("InvalidParameter", "Year parameter is not an integer", Constants.HTTP_BAD_REQUEST);
				return results;
			}
		}
		
		try {
			return referenceService.getReferenceSummaries(params);
		} catch (APIException e) {
			SearchResults<ReferenceSummaryDomain> out = new SearchResults<ReferenceSummaryDomain>();
			out.setError("Failed", "search failed: " + e.toString(), Constants.HTTP_SERVER_ERROR);
			return out;
		}
	}


	/* return domain object for single reference with given key
	 */
	@Override
	public SearchResults<ReferenceDomain> getValidReferenceCheck (String refsKey) {
		return this.getReferenceByKey(refsKey);
	}

	/* return domain object for single reference with given key
	 */
	@Override
	public SearchResults<ReferenceDomain> getReferenceByKey (String refsKey) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		if (refsKey != null) {
			Integer intRefsKey = null;
			try {
				intRefsKey = Integer.parseInt(refsKey);
			} catch (Throwable e) {
				results.setError("NotInteger", "Reference key not an integer: " + refsKey, Constants.HTTP_BAD_REQUEST);
				return results;
			}

			try {
				return referenceService.getReference(intRefsKey);
			} catch (APIException e) {
					results.setError("Failed", "Failed to get reference by key " + refsKey + ", exception: " + e.toString(),
						Constants.HTTP_NOT_FOUND);
			}
		} else {
			results.setError("InvalidParameter", "No reference key was specified", Constants.HTTP_BAD_REQUEST);
		}
		return results;
	}

	/* delete the reference with the given accession ID...  TODO: need to flesh this out, return SearchResults object, etc.
	 */
	@Override
	public SearchResults<ReferenceDomain> deleteReference(String api_access_token, String username, String id) {
		User currentUser = userService.getUser(username);
		if (currentUser != null) {
			return referenceService.deleteReference(id, currentUser);
		}
		return null;
	}
}
