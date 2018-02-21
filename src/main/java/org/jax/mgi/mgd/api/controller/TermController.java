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

import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/term")
@Api(value = "Term Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TermController extends BaseController<TermDomain> {

	@Inject
	private TermService termService;

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public TermDomain create(
			@ApiParam(value = "This is the passed in object")
			TermDomain term) {
		return termService.create(term);
	}

	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public TermDomain update(
			@ApiParam(value = "This is the passed in term object")
			TermDomain term) {
		return termService.update(term);
	}

	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public TermDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer key) {
		return termService.get(key);
	}
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public TermDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer term_key) {
		return termService.delete(term_key);
	}
	
	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<TermDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return termService.search(postParams, "sequenceNum");
	}
	
}
