package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.rest.interfaces.LogicalDBRESTInterface;
import org.jax.mgi.mgd.api.service.LogicalDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class LogicalDBController extends BaseController implements LogicalDBRESTInterface {

	@Inject
	private LogicalDBService logicaldbService;

	@Override
	public LogicalDBDomain create(String api_access_token, LogicalDBDomain logicaldb) {
		if(authenticate(api_access_token)) {
			return logicaldbService.create(logicaldb);
		}
		return null;
	}

	@Override
	public LogicalDBDomain update(String api_access_token, LogicalDBDomain logicaldb) {
		if(authenticate(api_access_token)) {
			return logicaldbService.update(logicaldb);
		}
		return null;
	}

	@Override
	public LogicalDBDomain get(Integer key) {
		return logicaldbService.get(key);
	}

	@Override
	public LogicalDBDomain delete(String api_access_token, Integer logicaldb_key) {
		if(authenticate(api_access_token)) {
			return logicaldbService.delete(logicaldb_key);
		}
		return null;
	}

	@Override
	public SearchResults<LogicalDBDomain> search(Map<String, Object> postParams) {
		return logicaldbService.search(postParams);

	}

}
