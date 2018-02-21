package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.service.LogicalDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class LogicalDBController extends BaseController<LogicalDBDomain> {

	@Inject
	private LogicalDBService logicaldbService;

	@Override
	public LogicalDBDomain create(LogicalDBDomain logicaldb) {
		return logicaldbService.create(logicaldb);
	}

	@Override
	public LogicalDBDomain update(LogicalDBDomain logicaldb) {
		return logicaldbService.update(logicaldb);
	}

	@Override
	public LogicalDBDomain get(Integer key) {
		return logicaldbService.get(key);
	}

	@Override
	public LogicalDBDomain delete(Integer logicaldb_key) {
		return logicaldbService.delete(logicaldb_key);
	}

	@Override
	public SearchResults<LogicalDBDomain> search(Map<String, Object> postParams) {
		return logicaldbService.search(postParams);

	}

}
