package org.jax.mgi.mgd.api.model.bib.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceBulkDomain;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/littriage")
@Api(value = "Lit Triage Endpoints for References")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LTReferenceRESTInterface {

	@PUT
	@ApiOperation(value = "Value: Update Reference", notes="Notes: Updates a Reference")
	public SearchResults<LTReferenceDomain> updateReference(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: This is the passed-in reference domain object")
			LTReferenceDomain reference
	);

	// Note that this is specifically for adding workflow tags for a set of reference keys.
	@PUT
	@Path("/bulkUpdate")
	@ApiOperation(value = "Value: Update list of References en masse", notes="Notes: Updates a list of References")
	public SearchResults<String> updateReferencesInBulk(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: reference keys and data to be updated")
			LTReferenceBulkDomain input
	);

	@PUT
	@Path("/statusUpdate")
	@ApiOperation(value = "Value: Update the status of a reference/workflow group pair", notes="Notes: Updates the status of a reference/workflow group pair")
	public SearchResults<String> updateReferenceStatus(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: comma-delimited list of accession IDs of references for which to set the status")
			@QueryParam("accid") String accid,
			
			@ApiParam(value = "Value: abbreviation of workflow group for which to set the status")
			@QueryParam("group") String group,
			
			@ApiParam(value = "Value: status term to set for the given reference/workflow group pair")
			@QueryParam("status") String status
	);

//	@GET
//	@Path("/valid")
//	@ApiOperation(value = "Value: Check to see if a reference is valid by doing a key-based lookup")
//	public SearchResults<LTReferenceDomain> getValidReferenceCheck(
//			@ApiParam(value = "Value: This is for searching by reference key")
//			@QueryParam("refsKey") String refsKey
//	);
//
//	@GET
//	@Path("/versions")
//	@ApiOperation(value = "Value: Get a list of valid choices for relevance version")
//	public List<String> getRelevanceVersions();

	@GET
	@Path("/{key}")
	@ApiOperation(value = "Value: Retrieve a single Reference by reference key")
	public SearchResults<LTReferenceDomain> getReferenceByKey (
			@ApiParam(value = "Value: This is for searching by reference key")
			@PathParam("key") String key);

}
