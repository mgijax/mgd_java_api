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

import org.jax.mgi.mgd.api.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/vocab")
@Api(value = "Vocab Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VocabController extends BaseController<VocabularyDomain> {

	@Inject
	private VocabService vocabService;

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public VocabularyDomain create(
			@ApiParam(value = "This is the passed in object")
			VocabularyDomain object) {
		return vocabService.create(object);
	}

	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public VocabularyDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer key) {
		return vocabService.get(key);
	}

	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public VocabularyDomain update(
			@ApiParam(value = "This is the passed in term object")
			VocabularyDomain object) {
		return vocabService.update(object);
	}

	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public VocabularyDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer key) {
		return vocabService.delete(key);
	}

	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<VocabularyDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return vocabService.search(postParams);
	}

}
