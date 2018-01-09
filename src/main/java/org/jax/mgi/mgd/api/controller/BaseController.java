package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.entities.ApiLogObject;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.service.ApiLogService;
import org.jax.mgi.mgd.api.service.UserService;

public class BaseController {

	@Inject
	private UserService userService;
	
	@Inject ApiLogService apiLogService;
	
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
	
	/* convenience method to remove any String parameters that have an empty string as the value
	 */
	protected Map<String, Object> filterEmptyParameters(Map<String, Object> params) {
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
	

	protected void logRequest(String endpoint, String parameters, String mgitype, List<Integer> objectKeys, User user) throws APIException {
		apiLogService.create(endpoint, parameters, mgitype, objectKeys, user);
	}

}
