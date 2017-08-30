package org.jax.mgi.mgd.api.rest.interfaces;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jax.mgi.mgd.api.entities.EntityBase;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public interface RESTInterface<T extends EntityBase> {
	
	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public T create(
		@ApiParam(value = "API Access Token used for Authentication to this API")
		@HeaderParam("api_access_token") String api_access_token,
		
		@ApiParam(value = "This is the passed in object")
		T object
	);
	
	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public T get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key") Integer key
	);
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public T update(
			@ApiParam(value = "API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "This is the passed in term object")
			T object
	);
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public T delete(
			@ApiParam(value = "API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "This Key will lookup and then delete it")
			@PathParam("key") Integer key
	);
	
	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<T> search(
		@ApiParam(value = "This is a map of the form parameters")
		Map<String, Object> postParams
	);
}
