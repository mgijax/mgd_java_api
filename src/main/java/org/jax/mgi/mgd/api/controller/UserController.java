package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.rest.interfaces.UserRESTInterface;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class UserController extends BaseController implements UserRESTInterface {

	@Inject
	private UserService userService;

	@Override
	public User create(String api_access_token, User user) {
		if(authenticate(api_access_token)) {
			return userService.create(user);
		}
		return null;
	}

	@Override
	public User update(String api_access_token, User user) {
		if(authenticate(api_access_token)) {
			return userService.update(user);
		}
		return null;
	}

	@Override
	public User get(Integer key) {
		return userService.get(key);
	}

	@Override
	public User delete(String api_access_token, Integer user_key) {
		if(authenticate(api_access_token)) {
			return userService.delete(user_key);
		}
		return null;
	}

	@Override
	public SearchResults<User> search(Map<String, Object> postParams) {
		return userService.search(postParams);

	}

}
