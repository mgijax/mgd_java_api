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

import org.jax.mgi.mgd.api.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.service.ActualDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/actualdb")
@Api(value = "ActualDB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActualDBController extends BaseController<ActualDBDomain> {

	@Inject
	private ActualDBService actualdbService;

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public ActualDBDomain create(
			@ApiParam(value = "This is the passed in object")
			ActualDBDomain actualdb) {
		return actualdbService.create(actualdb);
	}

	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public ActualDBDomain update(
			@ApiParam(value = "This is the passed in term object")
			ActualDBDomain actualdb) {
		return actualdbService.update(actualdb);
	}

	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public ActualDBDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer key) {
		return actualdbService.get(key);
	}

	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	public ActualDBDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer actualdb_key) {
		return actualdbService.delete(actualdb_key);
	}
	
	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<ActualDBDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return actualdbService.search(postParams);

	}

}
