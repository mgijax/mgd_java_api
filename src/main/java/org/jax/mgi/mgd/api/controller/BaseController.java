package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;

public class BaseController {

	protected boolean authenticate(String api_access_token) {
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
				if (((String) value).length() == 0) {
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
