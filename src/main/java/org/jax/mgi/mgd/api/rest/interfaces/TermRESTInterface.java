package org.jax.mgi.mgd.api.rest.interfaces;

import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jax.mgi.mgd.api.entities.Marker;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/term")
@Api(value = "Term Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TermRESTInterface {

	@POST
	@ApiOperation(value = "Create New Term", notes = "Notes: Term Create Notes")
	public Term createTerm(
		@ApiParam(value = "Value: API Access Token used for Authentication to this API")
		@HeaderParam("api_access_token") String api_access_token,
		
		@ApiParam(value = "Value: This is the passed in term object")
		Term term
	);
	
	@PUT
	@ApiOperation(value = "Value: Update Term", notes="Notes: Updates a Term")
	public Term updateTerm(
			@ApiParam(value = "Value: API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Value: This is the passed in term object")
			Term term
	);
	
	@POST
	@ApiOperation(value = "Search for Terms by Fields")
	@Path("/search")
	public SearchResults<Term> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, String> postParams
			);
	
	@GET
	@ApiOperation(value = "Value: Searches Terms by Fields", notes="Notes: Searches Term Fields")
	public SearchResults<Term> getTerm(
			@ApiParam(value = "Value: This is for searching by primary Id")
			@QueryParam("term_key") String term_key
	);
	
	@DELETE
	@ApiOperation(value = "Value: Deletes Term", notes="Notes: Deletes a Term")
	@Path("/{term_key}")
	public SearchResults<Term> deleteTerm(
			@ApiParam(value = "Value: API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Value: This Term Key will lookup a Term and then delete it")
			@PathParam("term_key") String term_key
	);
}
