package org.jax.mgi.mgd.api.model.mgi.interfaces;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.mgi.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/apilog")
@Api(value = "API Log Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ApiLogRESTInterface {
	@GET
	@ApiOperation(value = "Read", notes="Read")
	@Path("/{key}")
	public ApiLogDomain get(
			@ApiParam(value = "This is for retrieving by key")
			@PathParam("key") Integer key
	);
	

	@POST
	@ApiOperation(value = "Search by Fields")
	@Path("/search")
	public SearchResults<ApiLogDomain> search(
		@ApiParam(value = "This is a map of the form parameters")
		Map<String, Object> postParams
	);
}
