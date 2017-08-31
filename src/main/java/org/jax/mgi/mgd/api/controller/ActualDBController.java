package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.ActualDB;
import org.jax.mgi.mgd.api.rest.interfaces.ActualDBRESTInterface;
import org.jax.mgi.mgd.api.service.ActualDBService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class ActualDBController extends BaseController implements ActualDBRESTInterface {

	@Inject
	private ActualDBService actualdbService;
	
	private Logger log = Logger.getLogger(getClass());

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
