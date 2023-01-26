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
         * This adds a download endpoint to every category, e.g. assay/download, 
         * allele/download, marker/download, etc. 
         * The endpoint takes two parameters: the name of another endpoint (e.g., getAssayByRef),
         * and an identifier appropriate for that endpoint. Example:
         *    http://bhmgipwi01ld:8079/api/allele/download/getAlleleByMarker/MGI:97490
         *    http://bhmgipwi01ld:8079/api/allele/download/getAlleleByRef/J:326769
         *    http://bhmgipwi01ld:8079/api/marker/download/getMarkerByRef/J:326769
         * For downloading to work:
         * (1) the specified endpoint must either:
         *      a. Be an "all-at-once" endpoint. It takes a single accession id (String) parameter and return the 
         *         list of matching all domain objects. 
         *      b. Be a paginating endpoint. It takes an accession id (String) plus offset (int) and limit (int) 
         *         parameters which specify the chunk of the result set to return.
         *         Returns an object containing the metadata about the current chunk, plus the list of items
         *         in the current chunk (in a field named "items")
         * (2) The associated controller must implement a method named formatTsv that knows how to produce a line
         *     of TSV output from a query result row/object. formatTsv takes two arguments:
         *      1. the name of the endpoint generating the results
         *      2. a result object or null. If an object is passed, returns a line of TSV for that object.
         *         If null is passed, returns the line of column labels (i.e., the first line of the output).
         *     While the controller is free to implement the formatting function in any way it likes,
         *     for convenience, there is a helper method (below) that makes it easier to handle the common 
         *     case in which the TSV output is simply a subset of the fields (no additional processing allowed).
         */
	@GET
	@ApiOperation(value = "Download TSV file from a summary endpoint.")
	@Path("/download/{endpoint}/{accid}")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadEndpoint(
            @PathParam("endpoint") String endpoint,
            @PathParam("accid") String accid
        ) {
             String fname = this.getTsvFileName(endpoint, accid);
             return download(endpoint, accid, fname);
	}

        /*
         * The core of the download functionality.
         * Calls the specified endpoint with the given argument, then iterates 
         * through the results, writing to a tab-delimited format output stream.
         */
        private Response download (String endpoint, String arg, String fileName) {
            //
            log.info("download: " + endpoint + " " + arg + " " + fileName);
            //
            Method endpointMethod = null;
            boolean paginated = false;
            try {
                endpointMethod = this.getClass().getMethod(endpoint, String.class, int.class, int.class);
                paginated = true;
            }
            catch (Exception e) {
                try {
                    endpointMethod = this.getClass().getMethod(endpoint, String.class);
                    paginated = false;
                } catch (Exception ee) {
                    log.info("Cound not find endpoint: " + ee.toString());
                }
            }
            //
            final String endpoint_f = endpoint;
            final Method endpointMethod_f = endpointMethod;
            final boolean paginated_f = paginated;
            final BaseController<T> this_f = this;
            //
            StreamingOutput stream = new StreamingOutput() {
                @Override
                public void write(OutputStream os) throws IOException, WebApplicationException {
                    Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                    if (endpointMethod_f == null) {
                        writer.write("No endpoint.\n");
                        writer.flush();
                        return;
                    }
                    String firstLine = formatTsv(endpoint_f, null);
                    if (firstLine == null) {
                        writer.write("Not implemented.\n");
                        writer.flush();
                        return;
                    }
                    writer.write(firstLine);
                    List results = null;
                    if (paginated_f) {
                        int offset = 0;
                        int limit = 25000;
                        do {
                            try {
                                // paginated endpoints return SearchResults.
                                SearchResults res = (SearchResults) endpointMethod_f.invoke(this_f, arg, offset, limit);
                                results = res.items;
                            } catch (Exception e) {
                                log.info("Exception getting batch: " + e.toString());
                                break;
                            }
                            for (Object r : results) {
                                writer.write(formatTsv(endpoint_f, r));
                            }
                            offset += limit;
                        } while(results.size() > 0);
                    } else {
                        try {
                            // non-paginated endpoints return list of domains
                            results = (List) endpointMethod_f.invoke(this_f, arg);
                            for (Object r : results) {
                                writer.write(formatTsv(endpoint_f, r));
                            }
                        } catch (Exception e) {
                                log.info("Exception: " + e.toString());
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
         * Formats an object as a TSV row for the indicated endpoint.
         * OVERRIDE this function if you want to provide TSV downloads.
         */
        protected String formatTsv (String endpoint, Object obj) {
            return "Not-Implemented";
        }

        /*
         * Helper function available to subclasses implementing formatTsv.
         *
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
        protected String formatTsvHelper (Object obj, String[][] cols) {
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
                        Object v = f.get(obj);
                        val = (v == null ? "" : v.toString());
                    }
                    catch (IllegalAccessException e) {
                        log.info("Could not access field: " + fieldName);
                    }
                    catch (NoSuchFieldException e) {
                        log.info("No such field: " + fieldName);
                    }
                } else {
                    val = colName;
                }
                if (i > 0) b.append(separator);
                val = val.replaceAll("\t", " ").replaceAll("\n"," ");
                b.append(val);
            }
            b.append(terminator);
            return b.toString();
        }

        /*
         * Override this function if desired to generate a name for the downloaded file.
         * Default file name is "<endpoint>.<arg>.tsv"
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
