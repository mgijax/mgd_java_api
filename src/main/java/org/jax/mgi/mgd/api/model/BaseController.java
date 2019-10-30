package org.jax.mgi.mgd.api.model;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public abstract class BaseController<T extends BaseDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private UserService userService;
		
	/* if token is not defined in properties file, then do not require one.  
	 * Otherwise, must be an exact match (case sensitive).
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

	// get root exception of an exception
	protected static Throwable getRootException(Throwable exception) {
		Throwable rootException = exception;
		while(rootException.getCause() != null) {
		  rootException = rootException.getCause();
		}
		return rootException;
	}
	
	// if results.error is null, then API assumes success
	// if results.error is not null, then API assumes fail
	
	@POST
	@ApiOperation(value = "Create", notes = "Create")
	public SearchResults<T> create(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@ApiParam(value = "This is the passed in json object") T object) {

		SearchResults<T> results = new SearchResults<T>();
		
		try {
			log.info(Constants.LOG_IN_JSON);
			log.info(mapper.writeValueAsString(object));			
			
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				results = create(object, user);
				log.info(Constants.LOG_OUT_DOMAIN);
				if (results.items != null && !results.items.isEmpty()) {
					log.info(mapper.writeValueAsString(results.items.get(0)));
				}
			} else {
				results.setError(Constants.LOG_FAIL_USERAUTHENTICATION, api_access_token + "," + username, Constants.HTTP_SERVER_ERROR);
			}
		} catch (Exception e) {
			Throwable t = getRootException(e);
			StackTraceElement[] ste = t.getStackTrace();
			String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";
			
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);
			
		}
	
		return results;			
	}
	
	@GET
	@ApiOperation(value = "Read")
	@Path("/{key}")
	public T getByKey(@PathParam("key") @ApiParam(value = "This is for retrieving by key") Integer key) {
		return get(key);
	}
	
	@PUT
	@ApiOperation(value = "Update", notes="Update")
	public SearchResults<T> update(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@ApiParam(value = "This is the passed in json object") T object) {
		
		SearchResults<T> results = new SearchResults<T>();
		
		try {
			log.info(Constants.LOG_IN_JSON);
			log.info(mapper.writeValueAsString(object));			
			
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				results = update(object, user);
				log.info(Constants.LOG_OUT_DOMAIN);
				log.info(mapper.writeValueAsString(results.items.get(0)));					
			} else {
				results.setError(Constants.LOG_FAIL_USERAUTHENTICATION, api_access_token + "," + username, Constants.HTTP_SERVER_ERROR);
			}
		} catch (Exception e) {
			Throwable t = getRootException(e);
			StackTraceElement[] ste = t.getStackTrace();
			String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";
			
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);
		}
		
		return results;		
	}
	
	@DELETE
	@ApiOperation(value = "Delete")
	@Path("/{key}")
	public SearchResults<T> delete(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@PathParam("key") @ApiParam(value = "Delete object by primary key") Integer key) {
		
		SearchResults<T> results = new SearchResults<T>();
					
		try {
			log.info(Constants.LOG_IN_PKEY);
			log.info(mapper.writeValueAsString(key));			
			
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				results = delete(key, user);
				log.info(Constants.LOG_OUT_DOMAIN);
				if (results.items != null && !results.items.isEmpty()) {
				    log.info(mapper.writeValueAsString(results.items.size()));
				}
			} else {
				results.setError(Constants.LOG_FAIL_USERAUTHENTICATION, api_access_token + "," + username, Constants.HTTP_SERVER_ERROR);
			}
		} catch (Exception e) {
			Throwable t = getRootException(e);
			StackTraceElement[] ste = t.getStackTrace();
			String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";
			
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);
		}
	
		return results;
	}

	public abstract SearchResults<T> create(T object, User user);
	public abstract T get(Integer key);
	//this perhaps should be added to all controllers
	//public abstract SearchResults<T> getResults(Integer key);
	public abstract SearchResults<T> update(T object, User user);
	public abstract SearchResults<T> delete(Integer key, User user);

}
