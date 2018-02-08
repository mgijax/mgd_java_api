package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.rest.interfaces.MGITypeRESTInterface;
import org.jax.mgi.mgd.api.service.MGITypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class MGITypeController extends BaseController implements MGITypeRESTInterface {

	@Inject
	private MGITypeService mgitypeService;

	@Override
	public MGITypeDomain create(String api_access_token, MGITypeDomain mgitype) {
		if(authenticate(api_access_token)) {
			return mgitypeService.create(mgitype);
		}
		return null;
	}

	@Override
	public MGITypeDomain get(Integer key) {
		return mgitypeService.get(key);
	}

	@Override
	public MGITypeDomain update(String api_access_token, MGITypeDomain mgitype) {
		if(authenticate(api_access_token)) {
			return mgitypeService.update(mgitype);
		}
		return null;
	}

	@Override
	public MGITypeDomain delete(String api_access_token, Integer mgitype_key) {
		if(authenticate(api_access_token)) {
			return mgitypeService.delete(mgitype_key);
		}
		return null;
	}

	@Override
	public SearchResults<MGITypeDomain> search(Map<String, Object> postParams) {
		return mgitypeService.search(postParams);
	}

	
}
