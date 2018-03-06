package org.jax.mgi.mgd.api.model.acc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.SearchInterface;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.model.acc.search.LogicalDBSearchForm;
import org.jax.mgi.mgd.api.model.acc.service.LogicalDBService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/logicaldb")
@Api(value = "LogicalDB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogicalDBController extends BaseController<LogicalDBDomain> implements SearchInterface<LogicalDBDomain, LogicalDBSearchForm> {

	@Inject
	private LogicalDBService logicaldbService;

	public LogicalDBDomain create(LogicalDBDomain logicaldb, User user) {
		try {
			return logicaldbService.create(logicaldb, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public LogicalDBDomain update(LogicalDBDomain logicaldb, User user) {
		return logicaldbService.update(logicaldb, user);
	}

	public LogicalDBDomain get(Integer key) {
		return logicaldbService.get(key);
	}

	public LogicalDBDomain delete(Integer logicaldb_key, User user) {
		return logicaldbService.delete(logicaldb_key, user);
	}

	@Override
	public SearchResults<LogicalDBDomain> search(LogicalDBSearchForm searchForm) {
		return logicaldbService.search(searchForm);
	}

}
