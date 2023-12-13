package org.jax.mgi.mgd.api.model;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

public abstract class BaseController<T extends BaseDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject UserService userService;
		
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
	@Operation(description = "Create", summary = "Create")
	public SearchResults<T> create(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@Parameter(description = "This is the passed in json object") T object) {

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
			String message = "\n\nADD FAILED.\n\n" + t.toString();
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);	
		}
	
		return results;			
	}
	
	@GET
	@Operation(description = "Read")
	@Path("/{key}")
	public T getByKey(@PathParam("key") @Parameter(description = "This is for retrieving by key") Integer key) {
		return get(key);
	}
	
	@PUT
	@Operation(description = "Update", summary="Update")
	public SearchResults<T> update(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@Parameter(description = "This is the passed in json object") T object) {
		
		SearchResults<T> results = new SearchResults<T>();
		
		try {
			log.info(Constants.LOG_IN_JSON);
			log.info(mapper.writeValueAsString(object));			
			
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				results = update(object, user);
				log.info(Constants.LOG_OUT_DOMAIN);
				if (results.items != null && !results.items.isEmpty()) {
					log.info(mapper.writeValueAsString(results.items.get(0)));
				}				
			} else {
				results.setError(Constants.LOG_FAIL_USERAUTHENTICATION, api_access_token + "," + username, Constants.HTTP_SERVER_ERROR);
			}
		} catch (Exception e) {
			Throwable t = getRootException(e);
			//StackTraceElement[] ste = t.getStackTrace();
			//String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";
			String message = "\n\nMODIFY FAILED.\n\n" + t.toString();
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);
		}
		
		return results;		
	}
	
	@DELETE
	@Operation(description = "Delete")
	@Path("/{key}")
	public SearchResults<T> delete(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			@PathParam("key") @Parameter(description = "Delete object by primary key") Integer key) {
		
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
			//StackTraceElement[] ste = t.getStackTrace();
			//String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";		
			String message = "\n\nDELETE FAILED because this record is used elsewhere in the database.\n\n" + t.toString();
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
