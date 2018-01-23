package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.rest.interfaces.MGITypeRESTInterface;
import org.jax.mgi.mgd.api.service.MGITypeService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class MGITypeController extends BaseController implements MGITypeRESTInterface {

	@Inject
	private MGITypeService mgitypeService;
	
	private Logger log = Logger.getLogger(getClass());
	
	@Override
	public MGIType create(String api_access_token, MGIType mgitype) {
		if(authenticate(api_access_token)) {
			return mgitypeService.create(mgitype);
		}
		return null;
	}

	@Override
	public MGIType get(Integer key) {
		return mgitypeService.get(key);
	}

	@Override
	public MGIType update(String api_access_token, MGIType mgitype) {
		if(authenticate(api_access_token)) {
			return mgitypeService.update(mgitype);
		}
		return null;
	}

	@Override
	public MGIType delete(String api_access_token, Integer mgitype_key) {
		if(authenticate(api_access_token)) {
			return mgitypeService.delete(mgitype_key);
		}
		return null;
	}

	@Override
	public SearchResults<MGIType> search(Map<String, Object> postParams) {
		return mgitypeService.search(postParams);
	}

	
}
