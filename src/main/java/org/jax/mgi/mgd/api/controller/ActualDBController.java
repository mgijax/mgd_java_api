package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.service.ActualDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/actualdb")
@Api(value = "ActualDB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActualDBController extends BaseController<ActualDBDomain> {

	@Inject
	private ActualDBService actualdbService;

	public ActualDBDomain create(ActualDBDomain actualdb) {
		return actualdbService.create(actualdb);
	}

	public ActualDBDomain update(ActualDBDomain actualdb) {
		return actualdbService.update(actualdb);
	}

	public ActualDBDomain get(Integer key) {
		return actualdbService.get(key);
	}

	public ActualDBDomain delete(Integer actualdb_key) {
		return actualdbService.delete(actualdb_key);
	}

	public SearchResults<ActualDBDomain> search(Map<String, Object> postParams) {
		return actualdbService.search(postParams);

	}

}
