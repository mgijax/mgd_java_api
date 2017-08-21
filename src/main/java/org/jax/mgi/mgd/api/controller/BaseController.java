package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.service.UserService;

public class BaseController {

	@Inject
	private UserService userService;
	
	protected boolean authenticate(String api_access_token) {
		return true;
	}

	protected User getUser(String username) {
		return userService.getUser(username);
	}
	
	/* convenience method to remove any String parameters that have an empty string as the value
	 */
	protected HashMap<String, Object> filterEmptyParameters(HashMap<String, Object> params) {
		HashMap<String, Object> filtered = new HashMap<String, Object>();
		for (String key : params.keySet()) {
			boolean skipIt = false;
			Object value = params.get(key);
			if (value instanceof String) {
				if (((String) value).trim().length() == 0) {
					skipIt = true;
				}
			}
			if (!skipIt) {
				filtered.put(key, value);
			}
		}
		return filtered;
	}
}
