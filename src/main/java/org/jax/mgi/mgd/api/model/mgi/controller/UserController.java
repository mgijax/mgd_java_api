package org.jax.mgi.mgd.api.model.mgi.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.SearchInterface;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.UserSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/user")
@Api(value = "User Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController<UserDomain> implements SearchInterface<UserDomain, UserSearchForm> {

	@Inject
	private UserService userService;

	public UserDomain create(UserDomain userD, User user) {
		try {
			return userService.create(userD, user);
		} catch (APIException e) {
			e.printStackTrace();
			// Create HTTP error response
			return null;
		}
	}

	public UserDomain get(Integer key) {
		return userService.get(key);
	}

	public UserDomain update(UserDomain userD, User user) {
		return userService.update(userD, user);
	}

	public UserDomain delete(Integer user_key, User user) {
		return userService.delete(user_key, user);
	}

	@Override
	public SearchResults<UserDomain> search(UserSearchForm form) {
		// TODO Auto-generated method stub
		return userService.search(form);
	}

}
