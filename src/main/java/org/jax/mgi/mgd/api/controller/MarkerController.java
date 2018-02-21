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

import org.jax.mgi.mgd.api.domain.MarkerDomain;
import org.jax.mgi.mgd.api.service.MarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/marker")
@Api(value = "Marker Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerController extends BaseController<MarkerDomain> {

	@Inject
	private MarkerService markerService;
	
	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public MarkerDomain create(
			@ApiParam(value = "This is the passed in object")
			MarkerDomain marker) {
		return markerService.create(marker);
	}
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public MarkerDomain update(
			@ApiParam(value = "This is the passed in term object")
			MarkerDomain marker) {
		return markerService.update(marker);
	}
	
	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public MarkerDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer markerKey) {
		return markerService.get(markerKey);
	}
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	public MarkerDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer key) {
		return markerService.delete(key);
	}

	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<MarkerDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return markerService.search(postParams);
	}

}
