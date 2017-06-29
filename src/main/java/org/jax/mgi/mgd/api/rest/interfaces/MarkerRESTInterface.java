package org.jax.mgi.mgd.api.rest.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.entities.Marker;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/marker")
@Api(value = "Marker Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MarkerRESTInterface {

	@POST
	@ApiOperation(value = "Value: Create Marker", notes="Notes: Creates a new Marker")
	public Marker createMarker(
			@ApiParam(name = "Name: API Access Token", value = "Value: API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(name = "Name: Marker Object", value = "Value: This is the passed in marker object")
			Marker marker
	);
	
	@PUT
	@ApiOperation(value = "Value: Update Marker", notes="Notes: Updates a Marker")
	public Marker updateMarker(
			@ApiParam(name = "API Access Token", value = "Value: API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(name = "Name: Marker Object", value = "Value: This is the passed in marker object")
			Marker marker
	);

	@GET
	@ApiOperation(value = "Value: Searches Marker by Fields", notes="Notes: Searches Marker Fields")
	public List<Marker> getMarker(
			@ApiParam(name = "Name: primaryId", value = "Value: This is for searching by primary Id")
			@QueryParam("primaryId") String primaryId,
			
			@ApiParam(name = "Name: symbol", value = "Value: This is for searching by symbol")
			@QueryParam("symbol") String symbol);

	@DELETE
	@ApiOperation(value = "Value: Deletes Marker", notes="Notes: Deletes a Marker")
	@Path("/{id}")
	public Marker deleteMarker(
			@ApiParam(name = "API Access Token", value = "Value: API Access Token used for Authentication to this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(name = "Name: Marker ID", value = "Value: This Accession ID will lookup a Marker and then delete it")
			@PathParam("id") String id
	);

}
