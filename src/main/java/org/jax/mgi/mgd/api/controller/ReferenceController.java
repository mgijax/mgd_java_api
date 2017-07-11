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
	public SearchResults<Reference> getReference(String primaryId, String authors) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(primaryId != null) { map.put("primaryId", primaryId); }
		if(authors != null) { map.put("authors", authors); }
		log.info("Search Params: " + map);
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
