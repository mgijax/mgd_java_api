package org.jax.mgi.mgd.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.ReferenceBulkDomain;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.rest.interfaces.ReferenceRESTInterface;
import org.jax.mgi.mgd.api.service.ReferenceService;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.translators.ReferenceTranslator;
import org.jax.mgi.mgd.api.util.Constants;
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
	public ReferenceDomain createReference(String api_access_token, String username, ReferenceDomain reference) {
		User currentUser = userService.getUser(username);
		if (currentUser != null) {
			return referenceService.createReference(reference);
		}
		return null;
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
		if (currentUser != null) {
			// check that we found an actual user and didn't fall back to a default
			// (be extra careful here because of outside use)
			if (!currentUser.getLogin().equalsIgnoreCase(username)) {
				results.setError("Failed", "Unknown user: " + username, Constants.HTTP_BAD_REQUEST);
				return results;
			}
			
			try {
				// The updateReference method does not return the updated reference, as the method must finish
				// before the updates are persisted to the database.  So, we issue the update, then we use the
				// getReferenceByKey() method to re-fetch and return the updated object.
				
				HashMap<String,Object> searchFields = new HashMap<String,Object>();
				searchFields.put("accids", accid);
				SearchResults<Reference> refs = referenceService.getReference(searchFields);

				if (refs.total_count == 0) {
					results.setError("Failed", "No reference for ID " + accid, Constants.HTTP_BAD_REQUEST);

				} else if (refs.total_count > 1) {
					results.setError("Failed", "Multiple references for ID " + accid, Constants.HTTP_BAD_REQUEST);
					
				} else {
					ReferenceTranslator translator = new ReferenceTranslator(); 
					ReferenceDomain ref = translator.translate(refs.items.get(0));
					ref.setStatus(group, status);
					referenceService.updateReference(ref, currentUser);
					results.items = null;	// okay result
				}
			} catch (Throwable t) {
				results.setError("Failed", "Failed to save changes: " + t.toString(), Constants.HTTP_SERVER_ERROR);
			}
		} else {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED);
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
				// The updateReference method does not return the updated reference, as the method must finish
				// before the updates are persisted to the database.  So, we issue the update, then we use the
				// getReferenceByKey() method to re-fetch and return the updated object.
				
				if (referenceService.updateReferencesInBulk(input._refs_keys, input.workflow_tag, input.workflow_tag_operation,
						currentUser)) {
					results.items = null;	// okay result
				} else {
					results.setError("Failed", "Failed to save changes", Constants.HTTP_SERVER_ERROR);
				}
			} catch (Throwable t) {
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
	public SearchResults<ReferenceDomain> getReference(String accids, String allele_id, String authors, String date,
			String extracted_text,
			String isReviewArticle, String is_discard, String issue, String journal, String marker_id,
			String notes, String pages, String primary_author, String ref_abstract, String reference_type,
			Integer row_limit, String title, String volume, String workflow_tag_operator,
			String not_workflow_tag1, String workflow_tag1, String not_workflow_tag2, String workflow_tag2,
			String not_workflow_tag3, String workflow_tag3, String not_workflow_tag4, String workflow_tag4,
			String not_workflow_tag5, String workflow_tag5, String year, String status_operator,
			Integer status_AP_Chosen, Integer status_AP_Full_coded, Integer status_AP_Indexed,
			Integer status_AP_Not_Routed, Integer status_AP_Rejected, Integer status_AP_Routed,
			Integer status_GO_Chosen, Integer status_GO_Full_coded, Integer status_GO_Indexed,
			Integer status_GO_Not_Routed, Integer status_GO_Rejected, Integer status_GO_Routed,
			Integer status_GXD_Chosen, Integer status_GXD_Full_coded, Integer status_GXD_Indexed,
			Integer status_GXD_Not_Routed, Integer status_GXD_Rejected, Integer status_GXD_Routed,
			Integer status_QTL_Chosen, Integer status_QTL_Full_coded, Integer status_QTL_Indexed,
			Integer status_QTL_Not_Routed, Integer status_QTL_Rejected, Integer status_QTL_Routed,
			Integer status_Tumor_Chosen, Integer status_Tumor_Full_coded, Integer status_Tumor_Indexed,
			Integer status_Tumor_Not_Routed, Integer status_Tumor_Rejected, Integer status_Tumor_Routed,
			String sh_group, String sh_username, String sh_status, String sh_date
			) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (accids != null) { map.put("accids", accids); }
		if (allele_id != null) { map.put("allele_id", allele_id); }
		if (authors != null) { map.put("authors", authors); }
		if (date != null) { map.put("date", date); }
		if (extracted_text != null) { map.put("extracted_text", extracted_text); }
		if (issue != null) { map.put("issue", issue); }
		if (journal != null) { map.put("journal", journal); }
		if (marker_id != null) { map.put("marker_id", marker_id); }
		if (pages != null) { map.put("pages", pages); }
		if (primary_author != null) { map.put("primary_author", primary_author); }
		if (ref_abstract != null) { map.put("ref_abstract", ref_abstract); }
		if (row_limit != null) { map.put("row_limit", row_limit); }
		if (title != null) { map.put("title", title); }
		if (volume != null) { map.put("volume", volume); }
		if (workflow_tag_operator != null) { map.put("workflow_tag_operator", workflow_tag_operator); }
		if (not_workflow_tag1 != null) { map.put("not_workflow_tag1", not_workflow_tag1); }
		if (workflow_tag1 != null) { map.put("workflow_tag1", workflow_tag1); }
		if (not_workflow_tag2 != null) { map.put("not_workflow_tag2", not_workflow_tag2); }
		if (workflow_tag2 != null) { map.put("workflow_tag2", workflow_tag2); }
		if (not_workflow_tag3 != null) { map.put("not_workflow_tag3", not_workflow_tag3); }
		if (workflow_tag3 != null) { map.put("workflow_tag3", workflow_tag3); }
		if (not_workflow_tag4 != null) { map.put("not_workflow_tag4", not_workflow_tag4); }
		if (workflow_tag4 != null) { map.put("workflow_tag4", workflow_tag4); }
		if (not_workflow_tag5 != null) { map.put("not_workflow_tag5", not_workflow_tag5); }
		if (workflow_tag5 != null) { map.put("workflow_tag5", workflow_tag5); }
		if ((year != null) && (year.trim().length() != 0)) { map.put("year", year); }
		if (notes != null) { map.put("notes", notes); }
		if (reference_type != null) { map.put("reference_type", reference_type); }
		if (is_discard != null) { map.put("is_discard", is_discard); }

		if (isReviewArticle != null) {
			if ("0".equals(isReviewArticle) || "No".equalsIgnoreCase(isReviewArticle)) {
				map.put("isReviewArticle", 0);
			} else if ("1".equals(isReviewArticle) || "Yes".equalsIgnoreCase(isReviewArticle)) {
				map.put("isReviewArticle", 1);
			}
		}

		if (status_operator != null) { map.put("status_operator", status_operator); }
		if ((status_AP_Chosen != null) && (status_AP_Chosen == 1)) { map.put("status_AP_Chosen", 1); }
		if ((status_AP_Full_coded != null) && (status_AP_Full_coded == 1)) { map.put("status_AP_Full_coded", 1); }
		if ((status_AP_Indexed != null) && (status_AP_Indexed == 1)) { map.put("status_AP_Indexed", 1); }
		if ((status_AP_Not_Routed != null) && (status_AP_Not_Routed == 1)) { map.put("status_AP_Not_Routed", 1); }
		if ((status_AP_Rejected != null) && (status_AP_Rejected == 1)) { map.put("status_AP_Rejected", 1); }
		if ((status_AP_Routed != null) && (status_AP_Routed == 1)) { map.put("status_AP_Routed", 1); }

		if ((status_GO_Chosen != null) && (status_GO_Chosen == 1)) { map.put("status_GO_Chosen", 1); }
		if ((status_GO_Full_coded != null) && (status_GO_Full_coded == 1)) { map.put("status_GO_Full_coded", 1); }
		if ((status_GO_Indexed != null) && (status_GO_Indexed == 1)) { map.put("status_GO_Indexed", 1); }
		if ((status_GO_Not_Routed != null) && (status_GO_Not_Routed == 1)) { map.put("status_GO_Not_Routed", 1); }
		if ((status_GO_Rejected != null) && (status_GO_Rejected == 1)) { map.put("status_GO_Rejected", 1); }
		if ((status_GO_Routed != null) && (status_GO_Routed == 1)) { map.put("status_GO_Routed", 1); }

		if ((status_GXD_Chosen != null) && (status_GXD_Chosen == 1)) { map.put("status_GXD_Chosen", 1); }
		if ((status_GXD_Full_coded != null) && (status_GXD_Full_coded == 1)) { map.put("status_GXD_Full_coded", 1); }
		if ((status_GXD_Indexed != null) && (status_GXD_Indexed == 1)) { map.put("status_GXD_Indexed", 1); }
		if ((status_GXD_Not_Routed != null) && (status_GXD_Not_Routed == 1)) { map.put("status_GXD_Not_Routed", 1); }
		if ((status_GXD_Rejected != null) && (status_GXD_Rejected == 1)) { map.put("status_GXD_Rejected", 1); }
		if ((status_GXD_Routed != null) && (status_GXD_Routed == 1)) { map.put("status_GXD_Routed", 1); }

		if ((status_QTL_Chosen != null) && (status_QTL_Chosen == 1)) { map.put("status_QTL_Chosen", 1); }
		if ((status_QTL_Full_coded != null) && (status_QTL_Full_coded == 1)) { map.put("status_QTL_Full_coded", 1); }
		if ((status_QTL_Indexed != null) && (status_QTL_Indexed == 1)) { map.put("status_QTL_Indexed", 1); }
		if ((status_QTL_Not_Routed != null) && (status_QTL_Not_Routed == 1)) { map.put("status_QTL_Not_Routed", 1); }
		if ((status_QTL_Rejected != null) && (status_QTL_Rejected == 1)) { map.put("status_QTL_Rejected", 1); }
		if ((status_QTL_Routed != null) && (status_QTL_Routed == 1)) { map.put("status_QTL_Routed", 1); }

		if ((status_Tumor_Chosen != null) && (status_Tumor_Chosen == 1)) { map.put("status_Tumor_Chosen", 1); }
		if ((status_Tumor_Full_coded != null) && (status_Tumor_Full_coded == 1)) { map.put("status_Tumor_Full_coded", 1); }
		if ((status_Tumor_Indexed != null) && (status_Tumor_Indexed == 1)) { map.put("status_Tumor_Indexed", 1); }
		if ((status_Tumor_Not_Routed != null) && (status_Tumor_Not_Routed == 1)) { map.put("status_Tumor_Not_Routed", 1); }
		if ((status_Tumor_Rejected != null) && (status_Tumor_Rejected == 1)) { map.put("status_Tumor_Rejected", 1); }
		if ((status_Tumor_Routed != null) && (status_Tumor_Routed == 1)) { map.put("status_Tumor_Routed", 1); }

		// status history fields
		if (sh_group != null) { map.put("sh_group", sh_group); }
		if (sh_username != null) { map.put("sh_username", sh_username); }
		if (sh_status != null) { map.put("sh_status", sh_status); }
		if (sh_date != null) { map.put("sh_date", sh_date); }
		
		map = this.filterEmptyParameters(map);

		log.info("Search Params: " + map);
		
		// brief error checking

		if (map.containsKey("year")) {
			try {
				int intYear = Integer.parseInt((String) map.get("year"));
			} catch (Throwable t) {
				SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
				results.setError("InvalidParameter", "Year is not an integer: " + year, Constants.HTTP_BAD_REQUEST);
				return results;
			}
		}
		
		SearchResults<ReferenceDomain> domains = new SearchResults<ReferenceDomain>();

		SearchResults<Reference> results = referenceService.getReference(map);
		if (results.status_code != 200) {
			domains.setError(results.error, results.message, results.status_code);
		} else {
			// need to convert Reference entity objects to ReferenceDomain objects to meet PWI's needs
			
			ReferenceTranslator translator = new ReferenceTranslator(); 
			List<ReferenceDomain> domainObjects = new ArrayList<ReferenceDomain>();
			for (Reference ref : results.items) {
				domainObjects.add(translator.translate(ref));
			}
			domains.setItems(domainObjects);
		}
		return domains;
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
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				map.put("_refs_key", Integer.parseInt(refsKey));
			} catch (Throwable e) {
				results.setError("NotInteger", "Parameter value not an integer: " + refsKey, Constants.HTTP_BAD_REQUEST);
				return results;
			}

			SearchResults<Reference> refs = referenceService.getReference(map);
			if (refs.status_code != 200) {
				results.setError(refs.error, refs.message, refs.status_code);
				return results;
			}

			if (refs.total_count > 0) {
				ReferenceTranslator translator = new ReferenceTranslator(); 
				results.setItem(translator.translate(refs.items.get(0)));
			} else {
				results.setError("NotFound", "No reference with key " + refsKey, Constants.HTTP_NOT_FOUND);
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
			return referenceService.deleteReference(id);
		}
		return null;
	}
	
	/* get list of workflow status objects (current and historical) for the reference with the given key
	 */
	@Override
	public SearchResults<ReferenceWorkflowStatusDomain> getStatusHistoryByKey (String refsKey) {
		SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();

		// use lookup of reference to weed out and report parameter errors
		SearchResults<ReferenceDomain> referenceResult = this.getReferenceByKey(refsKey);
		if (referenceResult.error != null) {
			results.setError(referenceResult.error, referenceResult.message, referenceResult.status_code);
			return results;
		}

		results.setItems(referenceService.getStatusHistory(refsKey));
		return results;
	}
}
