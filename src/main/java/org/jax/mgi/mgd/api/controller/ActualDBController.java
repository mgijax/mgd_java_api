package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;
import org.jax.mgi.mgd.api.rest.interfaces.ActualDBRESTInterface;
import org.jax.mgi.mgd.api.service.ActualDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ActualDBController extends BaseController implements ActualDBRESTInterface {

	@Inject
	private ActualDBService actualdbService;

	@Override
	public ActualDB create(String api_access_token, ActualDB actualdb) {
		if(authenticate(api_access_token)) {
			return actualdbService.create(actualdb);
		}
		return null;
	}

	@Override
	public ActualDB update(String api_access_token, ActualDB actualdb) {
		if(authenticate(api_access_token)) {
			return actualdbService.update(actualdb);
		}
		return null;
	}

	@Override
	public ActualDB get(Integer key) {
		return actualdbService.get(key);
	}

	@Override
	public ActualDB delete(String api_access_token, Integer actualdb_key) {
		if(authenticate(api_access_token)) {
			return actualdbService.delete(actualdb_key);
		}
		return null;
	}

	@Override
	public SearchResults<ActualDB> search(Map<String, Object> postParams) {
		return actualdbService.search(postParams);

	}

}
