package org.jax.mgi.mgd.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.ApiLogService;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public abstract class BaseController<T extends BaseDomain> implements BaseRESTInterface<T> {

	@Inject
	private UserService userService;

	@Inject
	private ApiLogService apiLogService;
	
	//private Logger log = Logger.getLogger(getClass());
	
	/* if token is not defined in properties file, then do not require one.  Otherwise, must
	 * be an exact match (case sensitive).
	 */
	protected boolean authenticateToken(String api_access_token) {
		String expectedToken = System.getProperty("swarm.access_token");
		if (expectedToken != null) {
			return expectedToken.equals(api_access_token);
		}
		return true;
	}
	
	protected User authenticateUser(String username) {
		User user = userService.getUserByUsername(username);
		if(user == null) {
			return user;
		}
		return user;
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

	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public T create(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@ApiParam(value = "This is the passed in object") T object) {
		if(authenticateToken(api_access_token)) {
			User user = authenticateUser(username);
			if(user != null) {
				try {
					return create(object, user);
				} catch (APIException e) {
					// TODO Craft HTTP Response Code Error Message
					e.printStackTrace();
				}
			} else {
				// TODO Craft HTTP Response Code Error Message
			}
		} else {
			// TODO Craft HTTP Response Code Error Message
		}
		return null;
	}
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public T update(
			@HeaderParam(value="api_access_token for accessing the API") String api_access_token,
			@HeaderParam(value="Username of the logged in user") String username,
			@ApiParam(value = "This is the passed in object") T object) {
		if(authenticateToken(api_access_token)) {
			User user = authenticateUser(username);
			if(user != null) {
				try {
					return update(object, user);
				} catch (APIException e) {
					// TODO Craft HTTP Response Code Error Message
					e.printStackTrace();
				}
			} else {
				// TODO Craft HTTP Response Code Error Message
			}
		} else {
			// TODO Craft HTTP Response Code Error Message
		}
		return null;
	}
	
	@DELETE
	@ApiOperation(value = "Delete", notes="Delete")
	@Path("/{key}")
	public T delete(
			@HeaderParam(value="api_access_token for accessing the API") String api_access_token,
			@HeaderParam(value="Username of the logged in user") String username,
			@ApiParam(value = "This is for deleting by key") Integer key) {
		if(authenticateToken(api_access_token)) {
			User user = authenticateUser(username);
			if(user != null) {
				try {
					return delete(key, user);
				} catch (APIException e) {
					// TODO Craft HTTP Response Code Error Message
					e.printStackTrace();
				}
			} else {
				// TODO Craft HTTP Response Code Error Message
			}
		} else {
			// TODO Craft HTTP Response Code Error Message
		}
		return null;
	}
	
	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public T getByKey(
			@PathParam("key")
			@ApiParam(value = "This is for retrieving by key")
			Integer key) {
		return get(key);
	}

	public abstract T create(T object, User user) throws APIException;
	public abstract T get(Integer key);
	public abstract T update(T object, User user) throws APIException;
	public abstract T delete(Integer key, User user) throws APIException;


}
