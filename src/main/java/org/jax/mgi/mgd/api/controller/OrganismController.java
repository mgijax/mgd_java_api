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

import org.jax.mgi.mgd.api.domain.OrganismDomain;
import org.jax.mgi.mgd.api.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/organism")
@Api(value = "Organism Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganismController extends BaseController<OrganismDomain> {

	@Inject
	private OrganismService organismService;

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public OrganismDomain create(
			@ApiParam(value = "This is the passed in object")
			OrganismDomain organism) {
		return organismService.create(organism);
	}
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public OrganismDomain update(
			@ApiParam(value = "This is the passed in term object")
			OrganismDomain organism) {
		return organismService.update(organism);
	}

	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public OrganismDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer key) {
		return organismService.get(key);
	}
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public OrganismDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer organism_key) {
		return organismService.delete(organism_key);
	}
	
	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<OrganismDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return organismService.search(postParams);

	}

}
