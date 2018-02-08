package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.rest.interfaces.ActualDBRESTInterface;
import org.jax.mgi.mgd.api.service.ActualDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ActualDBController extends BaseController implements ActualDBRESTInterface {

	@Inject
	private ActualDBService actualdbService;

	@Override
	public ActualDBDomain create(String api_access_token, ActualDBDomain actualdb) {
		if(authenticate(api_access_token)) {
			return actualdbService.create(actualdb);
		}
		return null;
	}

	@Override
	public ActualDBDomain update(String api_access_token, ActualDBDomain actualdb) {
		if(authenticate(api_access_token)) {
			return actualdbService.update(actualdb);
		}
		return null;
	}

	@Override
	public ActualDBDomain get(Integer key) {
		return actualdbService.get(key);
	}

	@Override
	public ActualDBDomain delete(String api_access_token, Integer actualdb_key) {
		if(authenticate(api_access_token)) {
			return actualdbService.delete(actualdb_key);
		}
		return null;
	}

	@Override
	public SearchResults<ActualDBDomain> search(Map<String, Object> postParams) {
		return actualdbService.search(postParams);

	}

}
