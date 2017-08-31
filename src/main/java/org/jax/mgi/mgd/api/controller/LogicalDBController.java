package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.LogicalDB;
import org.jax.mgi.mgd.api.rest.interfaces.LogicalDBRESTInterface;
import org.jax.mgi.mgd.api.service.LogicalDBService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class LogicalDBController extends BaseController implements LogicalDBRESTInterface {

	@Inject
	private LogicalDBService logicaldbService;
	
	private Logger log = Logger.getLogger(getClass());

	@Override
	public LogicalDB create(String api_access_token, LogicalDB logicaldb) {
		if(authenticate(api_access_token)) {
			return logicaldbService.create(logicaldb);
		}
		return null;
	}

	@Override
	public LogicalDB update(String api_access_token, LogicalDB logicaldb) {
		if(authenticate(api_access_token)) {
			return logicaldbService.update(logicaldb);
		}
		return null;
	}

	@Override
	public LogicalDB get(Integer key) {
		return logicaldbService.get(key);
	}

	@Override
	public LogicalDB delete(String api_access_token, Integer logicaldb_key) {
		if(authenticate(api_access_token)) {
			return logicaldbService.delete(logicaldb_key);
		}
		return null;
	}

	@Override
	public SearchResults<LogicalDB> search(Map<String, Object> postParams) {
		return logicaldbService.search(postParams);

	}

}
