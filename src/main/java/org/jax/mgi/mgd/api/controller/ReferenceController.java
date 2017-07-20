package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.rest.interfaces.ReferenceRESTInterface;
import org.jax.mgi.mgd.api.service.ReferenceService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class ReferenceController extends BaseController implements ReferenceRESTInterface {

	@Inject
	private ReferenceService referenceService;
	
	private Logger log = Logger.getLogger(getClass());

	@Override
	public Reference createReference(String api_access_token, Reference reference) {
		if(authenticate(api_access_token)) {
			return referenceService.createReference(reference);
		} else {
			return null;
		}
	}

	@Override
	public Reference updateReference(String api_access_token, Reference reference) {
		if(authenticate(api_access_token)) {
			return referenceService.updateReference(reference);
		} else {
			return null;
		}
	}

	@Override
	public SearchResults<Reference> getReference(String accids, String allele_id, String authors, String date,
			Integer isReviewArticle, String issue, String journal, String marker_id,
			String notes, String pages, String primary_author, String ref_abstract, String reference_type,
			String title, String volume, Integer year, 
			Integer status_AP_Chosen, Integer status_AP_Fully_curated, Integer status_AP_Indexed,
			Integer status_AP_Not_Routed, Integer status_AP_Rejected, Integer status_AP_Routed,
			Integer status_GO_Chosen, Integer status_GO_Fully_curated, Integer status_GO_Indexed,
			Integer status_GO_Not_Routed, Integer status_GO_Rejected, Integer status_GO_Routed,
			Integer status_GXD_Chosen, Integer status_GXD_Fully_curated, Integer status_GXD_Indexed,
			Integer status_GXD_Not_Routed, Integer status_GXD_Rejected, Integer status_GXD_Routed,
			Integer status_QTL_Chosen, Integer status_QTL_Fully_curated, Integer status_QTL_Indexed,
			Integer status_QTL_Not_Routed, Integer status_QTL_Rejected, Integer status_QTL_Routed,
			Integer status_Tumor_Chosen, Integer status_Tumor_Fully_curated, Integer status_Tumor_Indexed,
			Integer status_Tumor_Not_Routed, Integer status_Tumor_Rejected, Integer status_Tumor_Routed
			) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (accids != null) { map.put("accids", accids); }
		if (allele_id != null) { map.put("allele_id", allele_id); }
		if (authors != null) { map.put("authors", authors); }
		if (date != null) { map.put("date", date); }
		if (isReviewArticle != null) { map.put("isReviewArticle", isReviewArticle); }
		if (issue != null) { map.put("issue", issue); }
		if (journal != null) { map.put("journal", journal); }
		if (marker_id != null) { map.put("marker_id", marker_id); }
		if (pages != null) { map.put("pages", pages); }
		if (primary_author != null) { map.put("primary_author", primary_author); }
		if (ref_abstract != null) { map.put("ref_abstract", ref_abstract); }
		if (title != null) { map.put("title", title); }
		if (volume != null) { map.put("volume", volume); }
		if (year != null) { map.put("year", year); }
		if (notes != null) { map.put("notes", notes); }
		if (reference_type != null) { map.put("reference_type", reference_type); }

		if ((status_AP_Chosen != null) && (status_AP_Chosen == 1)) { map.put("status_AP_Chosen", 1); }
		if ((status_AP_Fully_curated != null) && (status_AP_Fully_curated == 1)) { map.put("status_AP_Fully_curated", 1); }
		if ((status_AP_Indexed != null) && (status_AP_Indexed == 1)) { map.put("status_AP_Indexed", 1); }
		if ((status_AP_Not_Routed != null) && (status_AP_Not_Routed == 1)) { map.put("status_AP_Not_Routed", 1); }
		if ((status_AP_Rejected != null) && (status_AP_Rejected == 1)) { map.put("status_AP_Rejected", 1); }
		if ((status_AP_Routed != null) && (status_AP_Routed == 1)) { map.put("status_AP_Routed", 1); }

		if ((status_GO_Chosen != null) && (status_GO_Chosen == 1)) { map.put("status_GO_Chosen", 1); }
		if ((status_GO_Fully_curated != null) && (status_GO_Fully_curated == 1)) { map.put("status_GO_Fully_curated", 1); }
		if ((status_GO_Indexed != null) && (status_GO_Indexed == 1)) { map.put("status_GO_Indexed", 1); }
		if ((status_GO_Not_Routed != null) && (status_GO_Not_Routed == 1)) { map.put("status_GO_Not_Routed", 1); }
		if ((status_GO_Rejected != null) && (status_GO_Rejected == 1)) { map.put("status_GO_Rejected", 1); }
		if ((status_GO_Routed != null) && (status_GO_Routed == 1)) { map.put("status_GO_Routed", 1); }

		if ((status_GXD_Chosen != null) && (status_GXD_Chosen == 1)) { map.put("status_GXD_Chosen", 1); }
		if ((status_GXD_Fully_curated != null) && (status_GXD_Fully_curated == 1)) { map.put("status_GXD_Fully_curated", 1); }
		if ((status_GXD_Indexed != null) && (status_GXD_Indexed == 1)) { map.put("status_GXD_Indexed", 1); }
		if ((status_GXD_Not_Routed != null) && (status_GXD_Not_Routed == 1)) { map.put("status_GXD_Not_Routed", 1); }
		if ((status_GXD_Rejected != null) && (status_GXD_Rejected == 1)) { map.put("status_GXD_Rejected", 1); }
		if ((status_GXD_Routed != null) && (status_GXD_Routed == 1)) { map.put("status_GXD_Routed", 1); }

		if ((status_QTL_Chosen != null) && (status_QTL_Chosen == 1)) { map.put("status_QTL_Chosen", 1); }
		if ((status_QTL_Fully_curated != null) && (status_QTL_Fully_curated == 1)) { map.put("status_QTL_Fully_curated", 1); }
		if ((status_QTL_Indexed != null) && (status_QTL_Indexed == 1)) { map.put("status_QTL_Indexed", 1); }
		if ((status_QTL_Not_Routed != null) && (status_QTL_Not_Routed == 1)) { map.put("status_QTL_Not_Routed", 1); }
		if ((status_QTL_Rejected != null) && (status_QTL_Rejected == 1)) { map.put("status_QTL_Rejected", 1); }
		if ((status_QTL_Routed != null) && (status_QTL_Routed == 1)) { map.put("status_QTL_Routed", 1); }

		if ((status_Tumor_Chosen != null) && (status_Tumor_Chosen == 1)) { map.put("status_Tumor_Chosen", 1); }
		if ((status_Tumor_Fully_curated != null) && (status_Tumor_Fully_curated == 1)) { map.put("status_Tumor_Fully_curated", 1); }
		if ((status_Tumor_Indexed != null) && (status_Tumor_Indexed == 1)) { map.put("status_Tumor_Indexed", 1); }
		if ((status_Tumor_Not_Routed != null) && (status_Tumor_Not_Routed == 1)) { map.put("status_Tumor_Not_Routed", 1); }
		if ((status_Tumor_Rejected != null) && (status_Tumor_Rejected == 1)) { map.put("status_Tumor_Rejected", 1); }
		if ((status_Tumor_Routed != null) && (status_Tumor_Routed == 1)) { map.put("status_Tumor_Routed", 1); }

		log.info("Search Params: " + map);
		SearchResults.resetTimer();
		return new SearchResults<Reference>(referenceService.getReference(map));
	}


	@Override
	public SearchResults<Reference> getValidReferenceCheck (String refsKey) {
		return this.getReferenceByKey(refsKey);
	}

	@Override
	public SearchResults<Reference> getReferenceByKey (String refsKey) {
		SearchResults<Reference> results = new SearchResults<Reference>();
		SearchResults.resetTimer();
		if (refsKey != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				map.put("_refs_key", Long.parseLong(refsKey));
			} catch (Throwable e) {
				results.setError("NotInteger", "Parameter value not an integer: " + refsKey, Constants.HTTP_BAD_REQUEST);
				return results;
			}

			List<Reference> references = referenceService.getReference(map);
			if ((references != null) && (references.size() > 0)) {
				results.setItem(references.get(0));
			} else {
				results.setError("NotFound", "No reference with key " + refsKey, Constants.HTTP_NOT_FOUND);
			}
		} else {
			results.setError("InvalidParameter", "No reference key was specified", Constants.HTTP_BAD_REQUEST);
		}
		return results;
	}

	@Override
	public Reference deleteReference(String api_access_token, String id) {
		if(authenticate(api_access_token)) {
			return referenceService.deleteReference(id);
		} else {
			return null;
		}
	}
}
