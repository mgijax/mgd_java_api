package org.jax.mgi.mgd.api.rest.interfaces;

import java.util.Map;

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

import org.jax.mgi.mgd.api.domain.ReferenceBulkDomain;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowStatus;
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
	public SearchResults<ReferenceDomain> createReference(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: This is the passed-in reference object")
			ReferenceDomain reference
	);
	
	@PUT
	@ApiOperation(value = "Value: Update Reference", notes="Notes: Updates a Reference")
	public SearchResults<ReferenceDomain> updateReference(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: This is the passed-in reference domain object")
			ReferenceDomain reference
	);

	@PUT
	@Path("/bulkUpdate")
	@ApiOperation(value = "Value: Update list of References en masse", notes="Notes: Updates a list of References")
	public SearchResults<String> updateReferencesInBulk(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: reference keys and data to be updated")
			ReferenceBulkDomain input
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

	@GET
	@Path("/valid")
	@ApiOperation(value = "Value: Check to see if a reference is valid by doing a key-based lookup")
	public SearchResults<ReferenceDomain> getValidReferenceCheck(
			@ApiParam(value = "Value: This is for searching by reference key")
			@QueryParam("refsKey") String refsKey
	);

	@POST
	@Path("/search")
	@ApiOperation(value = "Value: Searches Reference by Fields", notes="Notes: Searches Reference Fields")
	public SearchResults<ReferenceDomain> search(
		@ApiParam(value = "This is a map of the form parameters")
		Map<String, Object> params								// see below for valid parameters
	);
	
	/* Acceptable reference search parameters:
	 *	accids : searches by any reference ID, case-insensitive, no wildcards
	 *	allele_id : searches by associated allele ID, case-insensitive, no wildcards
	 *	authors : search by authors, case-insensitive, wildcards allowed
	 *	date : search by date (free text), case-insensitive, wildcards allowed
	 *	extracted_text : search by extracted text, case-insensitive, AND search for all words in string
	 *	isReviewArticle : search for whether this is a review article (Yes/1) or not (No/0)
	 *	is_discard : search by value of is_discard flag (no discard, only discard, search all)
	 *	issue : search by issue field, case-insensitive, wildcards allowed
	 *	journal : search by journal field, case-insensitive, wildcards allowed
	 *	marker_id : searches by associated marker ID, case-insensitive, no wildcards
	 *	notes : search by reference notes field, case-insensitive, wildcards allowed
	 *	pages : search by pages field, case-insensitive, wildcards allowed
	 *	primary_author : search by primary author field, case-insensitive, wildcards allowed
	 *	ref_abstract : search by abstract field, case-insensitive, wildcards allowed
	 *	reference_type : search by reference type field, case-sensitive, no wildcards
	 *	row_limit : (integer) maximum number of rows to return (default is 1,001)
	 *	title : search by title field, case-insensitive, wildcards allowed
	 *	volume : search by volume field, case-insensitive, wildcards allowed
	 *	year : (integer) search by year field
	 *
	 *	workflow_tag_operator : operator to use when searching for multiple workflow tags (AND/OR)
	 *	not_workflow_tag1 : flag to indicate whether to apply a NOT operator (1/true) to the search for tag 1 or not (0/false)
	 *	workflow_tag1 : search by workflow tag 1, case-insensitive, no wildcards
	 *	not_workflow_tag2 : flag to indicate whether to apply a NOT operator (1/true) to the search for tag 2 or not (0/false)
	 *	workflow_tag2 : search by workflow tag 2, case-insensitive, no wildcards
	 *	not_workflow_tag3 : flag to indicate whether to apply a NOT operator (1/true) to the search for tag 3 or not (0/false)
	 *	workflow_tag3 : search by workflow tag 3, case-insensitive, no wildcards
	 *	not_workflow_tag4 : flag to indicate whether to apply a NOT operator (1/true) to the search for tag 4 or not (0/false)
	 *	workflow_tag4 : search by workflow tag 4, case-insensitive, no wildcards
	 *	not_workflow_tag5 : flag to indicate whether to apply a NOT operator (1/true) to the search for tag 5 or not (0/false)
	 *	workflow_tag5 : search by workflow tag 5, case-insensitive, no wildcards
	 *
	 *	status_operator : operator to use when searching across status field for multiple workflow groups (AND/OR)
	 *	status_AP_Chosen : flag to search for current status of Chosen for AP group (if field present = yes)
	 *	status_AP_Full_coded : flag to search for current status of Full-coded for AP group (if field present = yes)
	 *	status_AP_Indexed : flag to search for current status of Indexed for AP group (if field present = yes)
	 *	status_AP_Not_Routed : flag to search for current status of Not Routed for AP group (if field present = yes)
	 *	status_AP_Rejected : flag to search for current status of Rejected for AP group (if field present = yes)
	 *	status_AP_Routed : flag to search for current status of Routed for AP group (if field present = yes)
	 *	status_GO_Chosen : flag to search for current status of Chosen for GO group (if field present = yes)
	 *	status_GO_Full_coded : flag to search for current status of Full-coded for GO group (if field present = yes)
	 *	status_GO_Indexed : flag to search for current status of Indexed for GO group (if field present = yes)
	 *	status_GO_Not_Routed : flag to search for current status of Not Routed for GO group (if field present = yes)
	 *	status_GO_Rejected : flag to search for current status of Rejected for GO group (if field present = yes)
	 *	status_GO_Routed : flag to search for current status of Routed for GO group (if field present = yes)
	 *	status_GXD_Chosen : flag to search for current status of Chosen for GXD group (if field present = yes)
	 *	status_GXD_Full_coded : flag to search for current status of Full-coded for GXD group (if field present = yes)
	 *	status_GXD_Indexed : flag to search for current status of Indexed for GXD group (if field present = yes)
	 *	status_GXD_Not_Routed : flag to search for current status of Not Routed for GXD group (if field present = yes)
	 *	status_GXD_Rejected : flag to search for current status of Rejected for GXD group (if field present = yes)
	 *	status_GXD_Routed : flag to search for current status of Routed for GXD group (if field present = yes)
	 *	status_QTL_Chosen : flag to search for current status of Chosen for QTL group (if field present = yes)
	 *	status_QTL_Full_coded : flag to search for current status of Full-coded for QTL group (if field present = yes)
	 *	status_QTL_Indexed : flag to search for current status of Indexed for QTL group (if field present = yes)
	 *	status_QTL_Not_Routed : flag to search for current status of Not Routed for QTL group (if field present = yes)
	 *	status_QTL_Rejected : flag to search for current status of Rejected for QTL group (if field present = yes)
	 *	status_QTL_Routed : flag to search for current status of Routed for QTL group (if field present = yes)
	 *	status_Tumor_Chosen : flag to search for current status of Chosen for Tumor group (if field present = yes)
	 *	status_Tumor_Full_coded : flag to search for current status of Full-coded for Tumor group (if field present = yes)
	 *	status_Tumor_Indexed : flag to search for current status of Indexed for Tumor group (if field present = yes)
	 *	status_Tumor_Not_Routed : flag to search for current status of Not Routed for Tumor group (if field present = yes)
	 *	status_Tumor_Rejected : flag to search for current status of Rejected for Tumor group (if field present = yes)
	 *	status_Tumor_Routed : flag to search for current status of Routed for Tumor group (if field present = yes)
	 *
	 *  sh_group : search status history for status change in given workflow group, case-insensitive, no wildcards
	 *  sh_username : search status history for status change by given user, case-insensitive, no wildcards
	 *  sh_status : search status history for status change to the given status, case-insensitive, no wildcards
	 *  sh_date : search status history for status change with the given date criteria;
	 *  	date formats may be: mm/dd/yyyy, mm/dd/yy, or yyyy/mm/dd
	 *  	operators may be: =, <, >, <=, >=, or ..
	 */

	@GET
	@Path("/{refsKey}")
	@ApiOperation(value = "Value: Retrieve a single Reference by reference key")
	public SearchResults<ReferenceDomain> getReferenceByKey (
			@ApiParam(value = "Value: This is for searching by reference key")
			@PathParam("refsKey") String refsKey);

	@DELETE
	@ApiOperation(value = "Value: Deletes Reference", notes="Notes: Deletes a Reference")
	@Path("/{id}")
	public SearchResults<ReferenceDomain> deleteReference(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: This Accession ID will lookup a Reference and then delete it")
			@PathParam("id") String id
	);
}
