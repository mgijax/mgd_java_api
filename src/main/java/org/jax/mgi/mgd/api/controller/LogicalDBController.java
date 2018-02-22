package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.service.LogicalDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/logicaldb")
@Api(value = "LogicalDB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogicalDBController extends BaseController<LogicalDBDomain> {

	@Inject
	private LogicalDBService logicaldbService;

	public LogicalDBDomain create(LogicalDBDomain logicaldb) {
		return logicaldbService.create(logicaldb);
	}

	public LogicalDBDomain update(LogicalDBDomain logicaldb) {
		return logicaldbService.update(logicaldb);
	}

	public LogicalDBDomain getByKey(Integer key) {
		return logicaldbService.get(key);
	}

	public LogicalDBDomain delete(Integer logicaldb_key) {
		return logicaldbService.delete(logicaldb_key);
	}

	public SearchResults<LogicalDBDomain> searchByFields(Map<String, Object> postParams) {
		return logicaldbService.search(postParams);
	}

}
