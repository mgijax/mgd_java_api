package org.jax.mgi.mgd.api.model;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/* Start download functionality imports */
import java.util.List;
import java.lang.StringBuffer;
import java.lang.Exception;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException; 
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
/* End download functionality imports */

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
			String message = "\n\nADD FAILED.\n\n" + t.toString();
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
			//StackTraceElement[] ste = t.getStackTrace();
			//String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";		
			String message = "\n\nDELETE FAILED because this record is used elsewhere in the database.\n\n" + t.toString();
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);
		}
	
		return results;
	}

        /* *****************************************************************************
         * START download functionality section. Some of this stuff probably belongs 
         * somewhere else but for now, I'm parking everything here.
         * *****************************************************************************
         */

        /*
         * This adds a download endpoint to every category.
         * Example:
         *    http://bhmgipwi01ld:8079/api/allele/download/getAlleleByMarker/MGI:97490
         */
	@GET
	@ApiOperation(value = "Download TSV file of assays by marker, allele, etc.")
	@Path("/download/{endpoint}/{accid}")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadEndpoint(@PathParam("endpoint") String endpoint, @PathParam("accid") String accid) {
             String[][] cols = this.getTsvColumns(endpoint);
             String fname = this.getTsvFileName(endpoint, accid);
             return download(endpoint, accid, cols, fname);
	}

        /*
         * The core of the download functionality.
         * Calls the specified endpoint with the given argument, then iterates 
         * through the results, writing to a tab-delimited format output stream.
         */
        private Response download (String endpoint, String arg, String[][] columns, String fileName) {
            /* Call the specified endpoint with the given argument. */
            List results;
            try {
                Method endpointMethod = this.getClass().getMethod(endpoint, String.class);
                results = (List) endpointMethod.invoke(this, arg);
            }
            catch (Exception e) {
                results = null;
            }
            final List results_f = results;
            /* stream the output to keep memory footprint low. */
            StreamingOutput stream = new StreamingOutput() {
                @Override
                public void write(OutputStream os) throws IOException, WebApplicationException {
                    Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                    writer.write(formatTsvRow(null, columns));
                    if (results_f != null) {
                        for (Object r : results_f) {
                            writer.write(formatTsvRow(r, columns));
                        }
                    }
                    writer.flush();
                }
            };

            return Response
              .ok(stream)
              .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
              .build();
        }

        /*
         * Formats an object as a row in a tab-delimited output.
         * Caller passes the object and a list of columns. 
         * Returns a string.
         * To get a formatted header line containing the column labels,
         * pass null as the object.
         * Args:
         *   obj - the object to format, or null
         *   cols - a list of column specifiers. Each item is
         *        a list of 2 strings, specifying the column
         *        label and the correcponding object field.
         *        Example: [["ID", "accID"], ["Gene symbol", "symbol"], ["Gene name", "name"]]
         * Uses reflection to get the named field from the object. 
         * If an exception occurs (e.g., NoSuchFieldException) for any field, the
         * field's value is empty.
         */
        protected String formatTsvRow (Object obj, String[][] cols) {
            String separator = "\t";
            String terminator = "\n";
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < cols.length; i++) {
                String colName = cols[i][0];
                String fieldName = cols[i][1];

                String val = "";
                if (obj != null) {
                    try {
                        Field f = obj.getClass().getDeclaredField(fieldName);
                        f.setAccessible(true);
                        val = f.get(obj).toString();
                    }
                    catch (IllegalAccessException e) {
                        log.info("Could not access field: " + fieldName);
                    }
                    catch (NoSuchFieldException e) {
                        log.info("Could not access field: " + fieldName);
                    }
                } else {
                    val = colName;
                }
                if (i > 0) b.append(separator);
                b.append(val);
            }
            b.append(terminator);
            return b.toString();
        }

        /* 
         * Override this function in your controller to define the columns for tsv output.
         * Each column spec is a list of 2 strings: the column header label and the
         * corresponding object field name. Example:
         *     String [][] columns = { { "MGI ID", "accID" }, { "Gene symbol", "symbol" }, ... };
         */
        protected String[][] getTsvColumns (String endpoint) {
            String [][] columns = {};
            return columns;
        }

        /*
         * Override this function as desired to generate a name for the downloaded file.
         */
        protected String getTsvFileName (String endpoint, String arg) {
            return endpoint + "." + arg + ".tsv";
        }

        /* *****************************************************************************
         * END download functionality section.
         * *****************************************************************************
         */

	public abstract SearchResults<T> create(T object, User user);
	public abstract T get(Integer key);
	//this perhaps should be added to all controllers
	//public abstract SearchResults<T> getResults(Integer key);
	public abstract SearchResults<T> update(T object, User user);
	public abstract SearchResults<T> delete(Integer key, User user);

}
