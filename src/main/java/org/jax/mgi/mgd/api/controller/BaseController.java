package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.service.UserService;

public class BaseController {

	@Inject
	private UserService userService;
	
	/* if token is not defined in properties file, then do not require one.  Otherwise, must
	 * be an exact match (case sensitive).
	 */
	protected boolean authenticate(String api_access_token) {
		String expectedToken = System.getProperty("swarm.access_token");
		if (expectedToken != null) {
			return expectedToken.equals(api_access_token);
		}
		return true;
	}

	/* get the User object corresponding to the given username (Linux login);
	 * if not specified, fall back on mgd_dbo for now
	 * TODO: remove this fallback once we get farther along in development
	 */
	protected User getUser(String username) {
		if (username == null) {
			username = "mgd_dbo";
		}
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
