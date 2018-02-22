package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.UserDomain;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/user")
@Api(value = "User Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController<UserDomain> {

	@Inject
	private UserService userService;

	public UserDomain create(UserDomain user) {
		return userService.create(user);
	}

	public UserDomain getByKey(Integer key) {
		return userService.get(key);
	}

	public UserDomain update(UserDomain user) {
		return userService.update(user);
	}

	public UserDomain delete(Integer user_key) {
		return userService.delete(user_key);
	}
	
	public SearchResults<UserDomain> searchByFields(Map<String, Object> postParams) {
		return userService.search(postParams);
	}

}
