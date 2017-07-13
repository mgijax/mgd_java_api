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
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/reference")
@Api(value = "Reference Endpoints")
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
	@Path("/valid")
	@ApiOperation(value = "Value: Look up a single Reference by reference key")
	public Reference getReferenceByKey(
			@ApiParam(value = "Value: This is for searching by reference key")
			@QueryParam("refsKey") String refsKey);

	@GET
	@Path("/search")
	@ApiOperation(value = "Value: Searches Reference by Fields", notes="Notes: Searches Reference Fields")
	public SearchResults<Reference> getReference(
			@ApiParam(value = "Value: This is for searching by authors")
			@QueryParam("authors") String authors,
			
			@ApiParam(value = "Value: This is for searching by date")
			@QueryParam("date") String date,
			
			@ApiParam(value = "Value: This is for searching by is_review (0/1)")
			@QueryParam("is_review") Integer is_review,
			
			@ApiParam(value = "Value: This is for searching by issue")
			@QueryParam("issue") String issue,
			
			@ApiParam(value = "Value: This is for searching by pages")
			@QueryParam("pages") String pages,
			
			@ApiParam(value = "Value: This is for searching by primary_author")
			@QueryParam("primary_author") String primary_author,
			
			@ApiParam(value = "Value: This is for searching by abstract")
			@QueryParam("ref_abstract") String ref_abstract,
			
			@ApiParam(value = "Value: This is for searching by title")
			@QueryParam("title") String title,
			
			@ApiParam(value = "Value: This is for searching by volume")
			@QueryParam("volume") String volume,
			
			@ApiParam(value = "Value: This is for searching by year")
			@QueryParam("year") Integer year,
			
			@ApiParam(value = "1 = QTL workflow group has Chosen status")
			@QueryParam("status_QTL_Chosen") Integer status_QTL_Chosen
			);

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
