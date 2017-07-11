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

import org.jax.mgi.mgd.api.entities.Reference;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/reference")
@Api(value = "Reference Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReferenceRESTInterface {

	@POST
	@ApiOperation(value = "Value: Create Reference", notes="Notes: Creates a new Reference")
	public Reference createReference(
			@ApiParam(value = "Name: API Access Token")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Value: This is the passed-in reference object")
			Reference reference
	);
	
	@PUT
	@ApiOperation(value = "Value: Update Reference", notes="Notes: Updates a Reference")
	public Reference updateReference(
			@ApiParam(value = "API Access Token")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Value: This is the passed-in reference object")
			Reference reference
	);

	@GET
	@ApiOperation(value = "Value: Searches Reference by Fields", notes="Notes: Searches Reference Fields")
	public List<Reference> getReference(
			@ApiParam(value = "Value: This is for searching by primary Id")
			@QueryParam("primaryId") String primaryId,
			
			@ApiParam(value = "Value: This is for searching by authors")
			@QueryParam("authors") String authors);

	@DELETE
	@ApiOperation(value = "Value: Deletes Reference", notes="Notes: Deletes a Reference")
	@Path("/{id}")
	public Reference deleteReference(
			@ApiParam(value = "API Access Token")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Value: This Accession ID will lookup a Reference and then delete it")
			@PathParam("id") String id
	);
}
