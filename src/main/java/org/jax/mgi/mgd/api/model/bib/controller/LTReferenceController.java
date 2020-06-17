package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceBulkDomain;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.interfaces.LTReferenceRESTInterface;
import org.jax.mgi.mgd.api.model.bib.service.LTReferenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerService;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.service.TermService;
import org.jax.mgi.mgd.api.util.CommaSplitter;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.MapMaker;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class LTReferenceController extends BaseController<LTReferenceDomain> implements LTReferenceRESTInterface {

	/***--- instance variables ---***/
	
	@Inject
	private LTReferenceService referenceService;
	
	@Inject
	private TermService termService;
	
	@Inject
	private UserService userService;

	@Inject
	private MarkerService markerService;
	
	private Logger log = Logger.getLogger(getClass());

	/* These work together to allow for a maximum delay of two seconds for retries: */
	private static int maxRetries = 10;		// maximum number of retries for non-fatal exceptions on update operations
	private static int retryDelay = 200;	// number of ms to wait before retrying update operation after non-fatal exception
	
	/***--- methods ---***/
	
	/* update the given reference in the database, then return a revised version of it in the SearchResults
	 */
	@Override
	public SearchResults<LTReferenceDomain> updateReference(String api_access_token, String username, LTReferenceDomain reference) {
		SearchResults<LTReferenceDomain> results = new SearchResults<LTReferenceDomain>();

		if (!authenticateToken(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User currentUser = authenticateUser(username);
		if (currentUser != null) {
			try {
				// The updateReference method does not return the updated reference, as the method must finish
				// before the updates are persisted to the database.  So, we issue the update, then we use the
				// getReferenceByKey() method to re-fetch and return the updated object.
				
				boolean succeeded = false;
				int retries = 0;

				while (!succeeded) {
					try {
						referenceService.updateReference(reference, currentUser);
						
						// to update the mrk_reference cache table
						try {
							markerService.mrkrefByReferenceUtilities(reference.refsKey);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						
						succeeded = true;
					} catch (FatalAPIException f) {	// if it's definitely a fatal exception, just propagate it
						throw f;					
					} catch (APIException nf) {		// otherwise, try again (up to maxRetries times)
						retries++;
						if (retries > maxRetries) {
							throw nf;				// already tried all our allowed retries, so give up
						}
						Thread.sleep(retryDelay);
						log.info("Update: Retry #" + retries + " for " + reference.mgiid);
					}
				}

				return this.getReferenceByKey(reference.refsKey);
			} catch (Exception e) {
				Throwable t = getRootException(e);
				StackTraceElement[] ste = t.getStackTrace();
				String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";
				
				results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);
			}
//			} catch (Throwable t) {
//				results.setError("Failed", "Failed to save changes (" + t.toString() + ")", Constants.HTTP_SERVER_ERROR);
//			}
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

		if (!authenticateToken(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		// check that we have a legitimate status value

		if (status == null) {
			results.setError("Failed", "Unknown status value: null", Constants.HTTP_BAD_REQUEST);
			return results;
		} else {
			SearchResults<SlimTermDomain> terms = termService.validWorkflowStatus(status);
			if (terms.total_count == 0) {
				results.setError("Failed", "Unknown status term: " + status, Constants.HTTP_NOT_FOUND);
				return results;
			} else if (terms.total_count > 1) {
				results.setError("Failed", "Duplicate status terms: " + status, Constants.HTTP_BAD_REQUEST);
				return results;
			}
		}
		
		User currentUser = userService.getUserByUsername(username);
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
		List<String> referenceKeys = new ArrayList<String>();
		
		for (String myIDs : splitter.split(accid, 100)) {
			try {
				SearchResults<LTReferenceDomain> refs = referenceService.getReferences(mapMaker.toMap("{\"accids\" : \"" + myIDs + "\"}"));

				if (refs.total_count > 0) {
					for (LTReferenceDomain ref : refs.items) {
						int retries = 0;
						boolean moveOn = false;
						
						while (!moveOn) {
							try {
								currentID = ref.jnumid;
								ref.setStatus(group, status);
								referenceService.updateReference(ref, currentUser);
								referenceKeys.add(ref.refsKey);
								moveOn = true;
							} catch (FatalAPIException fe) {
								log.error("Could not save status for " + currentID + " (" + fe.toString() + ")");
								fe.printStackTrace();
								failures.add(currentID);
								moveOn = true;
							} catch (APIException e) {
								retries++;
								if (retries > maxRetries) {
									log.error("Could not save status for " + currentID + " (" + e.toString() + ")");
									e.printStackTrace();
									failures.add(currentID);
									moveOn = true;
								} else {
									try {
										Thread.sleep(retryDelay);
										log.info("UpdateStatus: Retry #" + retries + " for " + currentID);
									} catch (InterruptedException e1) {
										log.error("Could not save status for " + currentID + " (" + e1.toString() + ")");
										e1.printStackTrace();
										failures.add(currentID);
										moveOn = true;
									}
								}
							}
						}
					}
				}
			} catch (APIException t) {
				log.error("Failed to search for set of IDs" + t.toString());
				t.printStackTrace();
			}
		}

		if (failures.size() > 0) {
			results.setError("Partial Failure", "Status changes failed to save for: " + String.join(",", failures), Constants.HTTP_SERVER_ERROR);
		//} else {
		//	String json = "{\"group\":\"" + group + "\", \"status\":\"" + status + "\"}";
		}

		return results;
	}
	
	/* add the specified tag to each of the references specified by key
	 */
	@Override
	public SearchResults<String> updateReferencesInBulk (String api_access_token, String username, LTReferenceBulkDomain input) {
		SearchResults<String> results = new SearchResults<String>();

		if (!authenticateToken(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User currentUser = userService.getUserByUsername(username);
		if (currentUser != null) {
			try {
				referenceService.updateReferencesInBulk(input.refsKeys, input.workflow_tag, input.workflow_tag_operation, currentUser);
				results.items = null;	// okay result
			} catch (APIException t) {
				results.setError("Failed", "Failed to save changes: " + t.toString(), Constants.HTTP_SERVER_ERROR);
			//} catch (JsonProcessingException t) {
			//	results.setError("Log Failure", "Changes saved, but failed to log them in API log: " + t.toString(), Constants.HTTP_SERVER_ERROR);
			}
		} else {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED);
		}
		return results;
	}
	
	/* search method - retrieves references based on query form parameters
	 */
	@Override
	public SearchResults<LTReferenceSummaryDomain> search(Map<String,Object> params) {
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
		
		try {
			return referenceService.getReferenceSummaries(params);
		} catch (APIException e) {
			SearchResults<LTReferenceSummaryDomain> out = new SearchResults<LTReferenceSummaryDomain>();
			out.setError("Failed", "search failed: " + e.toString(), Constants.HTTP_SERVER_ERROR);
			return out;
		}
	}


	/* return domain object for single reference with given key
	 */
	@Override
	public SearchResults<LTReferenceDomain> getValidReferenceCheck (String refsKey) {
		return this.getReferenceByKey(refsKey);
	}

	/* return domain object for single reference with given key
	 */
	@Override
	public SearchResults<LTReferenceDomain> getReferenceByKey (String key) {
		SearchResults<LTReferenceDomain> results = new SearchResults<LTReferenceDomain>();
		if (key != null) {
			try {
				return referenceService.getReference(key);
			} catch (APIException e) {
					results.setError("Failed", "Failed to get reference by key " + key + ", exception: " + e.toString(),
						Constants.HTTP_NOT_FOUND);
			}
		} else {
			results.setError("InvalidParameter", "No reference key was specified", Constants.HTTP_BAD_REQUEST);
		}
		return results;
	}

	// never used/always use the ReferenceController/create
	@Override
	public SearchResults<LTReferenceDomain> create(LTReferenceDomain object, User user) {
		return null;
	}

	// never used
	@Override
	public LTReferenceDomain get(Integer key) {
		return null;
	}

	// never useds	
	@Override
	public SearchResults<LTReferenceDomain> update(LTReferenceDomain object, User user) {
		return null;
	}

	// never used/always use the ReferenceController/delete
	@Override
	public SearchResults<LTReferenceDomain> delete(Integer key, User user) {
		return null;
	}
}
