package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.UserDomain;
import org.jax.mgi.mgd.api.rest.interfaces.UserRESTInterface;
import org.jax.mgi.mgd.api.service.UserService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class UserController extends BaseController implements UserRESTInterface {

	@Inject
	private UserService userService;

	@Override
	public UserDomain create(String api_access_token, UserDomain user) {
		if(authenticate(api_access_token)) {
			return userService.create(user);
		}
		return null;
	}

	@Override
	public UserDomain get(Integer key) {
		return userService.get(key);
	}
	
	@Override
	public UserDomain update(String api_access_token, UserDomain user) {
		if(authenticate(api_access_token)) {
			return userService.update(user);
		}
		return null;
	}

	@Override
	public UserDomain delete(String api_access_token, Integer user_key) {
		if(authenticate(api_access_token)) {
			return userService.delete(user_key);
		}
		return null;
	}

	@Override
	public SearchResults<UserDomain> search(Map<String, Object> postParams) {
		return userService.search(postParams);
	}

}
