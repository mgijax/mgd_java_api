package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/user")
@Tag(name = "User Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController<UserDomain> {

	@Inject
	private UserService userService;

	@Override
	public SearchResults<UserDomain> create(UserDomain userD, User user) {
		return userService.create(userD, user);
	}

	@Override
	public SearchResults<UserDomain> update(UserDomain userD, User user) {
		return userService.update(userD, user);
	}

	@Override
	public UserDomain get(Integer key) {
		return userService.get(key);
	}

	@Override
	public SearchResults<UserDomain> delete(Integer key, User user) {
		return userService.delete(key, user);
	}

	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<UserDomain> search() {
		return userService.search();
	}
	
}
