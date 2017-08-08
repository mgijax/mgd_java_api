package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;

public class BaseController {

	protected boolean authenticate(String api_access_token) {
		return true;
	}
	
	/* convenience method to remove any String parameters that have an empty string as the value
	 */
	protected void removeEmptyParameters(HashMap<String, Object> params) {
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value instanceof String) {
				if (((String) value).length() == 0) {
					params.remove(key);
				}
			}
		}
	}
}
