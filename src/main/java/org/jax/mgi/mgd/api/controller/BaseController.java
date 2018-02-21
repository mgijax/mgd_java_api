package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;
import org.jax.mgi.mgd.api.service.ApiLogService;
import org.jax.mgi.mgd.api.util.SearchResults;

public abstract class BaseController<T extends DomainBase> implements RESTInterface<T> {

	//@Inject
	//private UserService userService;

	@Inject ApiLogService apiLogService;

	/* if token is not defined in properties file, then do not require one.  Otherwise, must
	 * be an exact match (case sensitive).
	 */
	protected boolean authenticate(String api_access_token, String username) {
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

	@Override
	public T create(String api_access_token, String username, T object) {
		if(authenticate(api_access_token, username)) {
			return create(object);
		}
		return null;
	}
	
	public abstract T create(T object);
	
	public abstract T get(Integer key);

	@Override
	public T update(String api_access_token, String username, T object) {
		if(authenticate(api_access_token, username)) {
			return update(object);
		}
		return null;
	}
	
	public abstract T update(T object);

	@Override
	public T delete(String api_access_token, String username, Integer key) {
		if(authenticate(api_access_token, username)) {
			return delete(key);
		}
		return null;
	}
	
	public abstract T delete(Integer key);

	public abstract SearchResults<T> search(Map<String, Object> postParams);



}
