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

import org.jax.mgi.mgd.api.domain.UserDomain;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/user")
@Api(value = "User Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController<UserDomain> {

	@Inject
	private UserService userService;

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public UserDomain create(
			@ApiParam(value = "This is the passed in object")
			UserDomain user) {
		return userService.create(user);
	}
	
	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public UserDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key")
			Integer key) {
		return userService.get(key);
	}
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public UserDomain update(
			@ApiParam(value = "This is the passed in term object")
			UserDomain user) {
		return userService.update(user);
	}
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public UserDomain delete(
			@ApiParam(value = "This Key will lookup and then delete it")
			Integer user_key) {
		return userService.delete(user_key);
	}
	
	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<UserDomain> search(
			@ApiParam(value = "This is a map of the form parameters")
			Map<String, Object> postParams) {
		return userService.search(postParams);
	}

}
