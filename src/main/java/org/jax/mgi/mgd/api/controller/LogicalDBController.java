package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.service.LogicalDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/logicaldb")
@Api(value = "LogicalDB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogicalDBController extends BaseController<LogicalDBDomain> {

	@Inject
	private LogicalDBService logicaldbService;

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public LogicalDBDomain create(
			@ApiParam(value = "This is the passed in object")
			LogicalDBDomain logicaldb) {
		return logicaldbService.create(logicaldb);
	}
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public LogicalDBDomain update(
			@ApiParam(value = "This is the passed in term object")
			LogicalDBDomain logicaldb) {
		return logicaldbService.update(logicaldb);
	}
	
	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public LogicalDBDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer key) {
		return logicaldbService.get(key);
	}
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public LogicalDBDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer logicaldb_key) {
		return logicaldbService.delete(logicaldb_key);
	}
	
	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<LogicalDBDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return logicaldbService.search(postParams);

	}

}
