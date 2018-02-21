package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.UserDomain;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class UserController extends BaseController<UserDomain> {

	@Inject
	private UserService userService;

	@Override
	public UserDomain create(UserDomain user) {
		return userService.create(user);
	}

	@Override
	public UserDomain get(Integer key) {
		return userService.get(key);
	}
	
	@Override
	public UserDomain update(UserDomain user) {
		return userService.update(user);
	}

	@Override
	public UserDomain delete(Integer user_key) {
		return userService.delete(user_key);
	}

	@Override
	public SearchResults<UserDomain> search(Map<String, Object> postParams) {
		return userService.search(postParams);
	}

}
