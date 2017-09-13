package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.entities.ApiTableLog;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.service.ApiEventLogService;
import org.jax.mgi.mgd.api.service.UserService;

public class BaseController {

	@Inject
	private UserService userService;
	
	@Inject ApiEventLogService apiLogService;
	
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
	
	protected void logRequest(String endpoint, DomainBase domain) {
		apiLogService.create(endpoint, domain);
	}
}
