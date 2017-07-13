package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.rest.interfaces.ReferenceRESTInterface;
import org.jax.mgi.mgd.api.service.ReferenceService;
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
	public SearchResults<Reference> getReference(String authors, String date, Integer is_review, String issue,
			String pages, String primary_author, String ref_abstract, String title, String volume, Integer year,
			Integer status_QTL_Chosen) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (authors != null) { map.put("authors", authors); }
		if (date != null) { map.put("date", date); }
		if (is_review != null) { map.put("is_review", is_review); }
		if (issue != null) { map.put("issue", issue); }
		if (pages != null) { map.put("pages", pages); }
		if (primary_author != null) { map.put("primary_author", primary_author); }
		if (ref_abstract != null) { map.put("ref_abstract", ref_abstract); }
		if (title != null) { map.put("title", title); }
		if (volume != null) { map.put("volume", volume); }
		if (year != null) { map.put("year", year); }
		if ((status_QTL_Chosen != null) && (status_QTL_Chosen == 1)) { map.put("status_QTL_Chosen", status_QTL_Chosen); }
		log.info("Search Params: " + map);
		SearchResults.resetTimer();
		return new SearchResults<Reference>(referenceService.getReference(map));
	}

	@Override
	public Reference deleteReference(String api_access_token, String id) {
		if(authenticate(api_access_token)) {
			return referenceService.deleteReference(id);
		} else {
			return null;
		}
	}

	@Override
	public Reference getReferenceByKey (String refsKey) {
		if (refsKey != null) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("_refs_key", Long.parseLong(refsKey));
			return referenceService.getReference(map).get(0);
		}
		return null;
	}

}
