package org.jax.mgi.mgd.api.rest.interfaces;

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
	public Reference createReference(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: This is the passed-in reference object")
			Reference reference
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

	@GET
	@Path("/valid")
	@ApiOperation(value = "Value: Check to see if a reference is valid by doing a key-based lookup")
	public SearchResults<ReferenceDomain> getValidReferenceCheck(
			@ApiParam(value = "Value: This is for searching by reference key")
			@QueryParam("refsKey") String refsKey
	);

	@GET
	@Path("/search")
	@ApiOperation(value = "Value: Searches Reference by Fields", notes="Notes: Searches Reference Fields")
	public SearchResults<ReferenceDomain> getReference(
			@ApiParam(value = "Value: This is for searching by reference IDs")
			@QueryParam("accids") String accids,
			
			@ApiParam(value = "Value: This is for searching by allele ID")
			@QueryParam("allele_id") String allele_id,
			
			@ApiParam(value = "Value: This is for searching by authors")
			@QueryParam("authors") String authors,
			
			@ApiParam(value = "Value: This is for searching by date")
			@QueryParam("date") String date,
			
			@ApiParam(value = "Value: This is for searching by extracted text")
			@QueryParam("extracted_text") String extracted_text,
			
			@ApiParam(value = "Value: This is for searching by isReviewArticle (0/1) or (No/Yes)")
			@QueryParam("isReviewArticle") String isReviewArticle,
			
			@ApiParam(value = "Value: This is for searching by discard flag (no discard, only discard, search all)")
			@QueryParam("is_discard") String is_discard,
			
			@ApiParam(value = "Value: This is for searching by issue")
			@QueryParam("issue") String issue,
			
			@ApiParam(value = "Value: This is for searching by journal")
			@QueryParam("journal") String journal,
			
			@ApiParam(value = "Value: This is for searching by marker ID")
			@QueryParam("marker_id") String marker_id,
			
			@ApiParam(value = "Value: This is for searching by notes")
			@QueryParam("notes") String notes,
			
			@ApiParam(value = "Value: This is for searching by pages")
			@QueryParam("pages") String pages,
			
			@ApiParam(value = "Value: This is for searching by primary_author")
			@QueryParam("primary_author") String primary_author,
			
			@ApiParam(value = "Value: This is for searching by abstract")
			@QueryParam("ref_abstract") String ref_abstract,
			
			@ApiParam(value = "Value: This is for searching by reference type")
			@QueryParam("reference_type") String reference_type,
			
			@ApiParam(value = "Value: Max rows to return (default is 1001)")
			@QueryParam("row_limit") Integer row_limit,
			
			@ApiParam(value = "Value: This is for searching by title")
			@QueryParam("title") String title,
			
			@ApiParam(value = "Value: This is for searching by volume")
			@QueryParam("volume") String volume,
			
			@ApiParam(value = "Value: Specify 'AND' or 'OR' to specify operator for searching multiple workflow_tags")
			@QueryParam("workflow_tag_operator") String workflow_tag_operator,
			
			@ApiParam(value = "Value: Specify 'NOT' to use NOT opeator for workflow_tag1")
			@QueryParam("not_workflow_tag1") String not_workflow_tag1,
			
			@ApiParam(value = "Value: This is for searching by workflow tag1")
			@QueryParam("workflow_tag1") String workflow_tag1,
			
			@ApiParam(value = "Value: Specify 'NOT' to use NOT opeator for workflow_tag2")
			@QueryParam("not_workflow_tag2") String not_workflow_tag2,
			
			@ApiParam(value = "Value: This is for searching by workflow tag2")
			@QueryParam("workflow_tag2") String workflow_tag2,
			
			@ApiParam(value = "Value: Specify 'NOT' to use NOT opeator for workflow_tag3")
			@QueryParam("not_workflow_tag3") String not_workflow_tag3,
			
			@ApiParam(value = "Value: This is for searching by workflow tag3")
			@QueryParam("workflow_tag3") String workflow_tag3,
			
			@ApiParam(value = "Value: Specify 'NOT' to use NOT opeator for workflow_tag4")
			@QueryParam("not_workflow_tag4") String not_workflow_tag4,
			
			@ApiParam(value = "Value: This is for searching by workflow tag4")
			@QueryParam("workflow_tag4") String workflow_tag4,
			
			@ApiParam(value = "Value: Specify 'NOT' to use NOT opeator for workflow_tag5")
			@QueryParam("not_workflow_tag5") String not_workflow_tag5,
			
			@ApiParam(value = "Value: This is for searching by workflow tag5")
			@QueryParam("workflow_tag5") String workflow_tag5,
			
			@ApiParam(value = "Value: This is for searching by year")
			@QueryParam("year") String year,
			
			@ApiParam(value = "1 = AP workflow group has Chosen status")
			@QueryParam("status_AP_Chosen") Integer status_AP_Chosen,
			
			@ApiParam(value = "1 = AP workflow group has Fully curated status")
			@QueryParam("status_AP_Full_coded") Integer status_AP_Full_coded,
			
			@ApiParam(value = "1 = AP workflow group has Indexed status")
			@QueryParam("status_AP_Indexed") Integer status_AP_Indexed,
			
			@ApiParam(value = "1 = AP workflow group has Not Routed status")
			@QueryParam("status_AP_Not_Routed") Integer status_AP_Not_Routed,
			
			@ApiParam(value = "1 = AP workflow group has Rejected status")
			@QueryParam("status_AP_Rejected") Integer status_AP_Rejected,
			
			@ApiParam(value = "1 = AP workflow group has Routed status")
			@QueryParam("status_AP_Routed") Integer status_AP_Routed,
			
			@ApiParam(value = "1 = GO workflow group has Chosen status")
			@QueryParam("status_GO_Chosen") Integer status_GO_Chosen,
			
			@ApiParam(value = "1 = GO workflow group has Fully curated status")
			@QueryParam("status_GO_Full_coded") Integer status_GO_Full_coded,
			
			@ApiParam(value = "1 = GO workflow group has Indexed status")
			@QueryParam("status_GO_Indexed") Integer status_GO_Indexed,
			
			@ApiParam(value = "1 = GO workflow group has Not Routed status")
			@QueryParam("status_GO_Not_Routed") Integer status_GO_Not_Routed,
			
			@ApiParam(value = "1 = GO workflow group has Rejected status")
			@QueryParam("status_GO_Rejected") Integer status_GO_Rejected,
			
			@ApiParam(value = "1 = GO workflow group has Routed status")
			@QueryParam("status_GO_Routed") Integer status_GO_Routed,
			
			@ApiParam(value = "1 = GXD workflow group has Chosen status")
			@QueryParam("status_GXD_Chosen") Integer status_GXD_Chosen,
			
			@ApiParam(value = "1 = GXD workflow group has Fully curated status")
			@QueryParam("status_GXD_Full_coded") Integer status_GXD_Full_coded,
			
			@ApiParam(value = "1 = GXD workflow group has Indexed status")
			@QueryParam("status_GXD_Indexed") Integer status_GXD_Indexed,
			
			@ApiParam(value = "1 = GXD workflow group has Not Routed status")
			@QueryParam("status_GXD_Not_Routed") Integer status_GXD_Not_Routed,
			
			@ApiParam(value = "1 = GXD workflow group has Rejected status")
			@QueryParam("status_GXD_Rejected") Integer status_GXD_Rejected,
			
			@ApiParam(value = "1 = GXD workflow group has Routed status")
			@QueryParam("status_GXD_Routed") Integer status_GXD_Routed,
			
			@ApiParam(value = "1 = QTL workflow group has Chosen status")
			@QueryParam("status_QTL_Chosen") Integer status_QTL_Chosen,
			
			@ApiParam(value = "1 = QTL workflow group has Fully curated status")
			@QueryParam("status_QTL_Full_coded") Integer status_QTL_Full_coded,
			
			@ApiParam(value = "1 = QTL workflow group has Indexed status")
			@QueryParam("status_QTL_Indexed") Integer status_QTL_Indexed,
			
			@ApiParam(value = "1 = QTL workflow group has Not Routed status")
			@QueryParam("status_QTL_Not_Routed") Integer status_QTL_Not_Routed,
			
			@ApiParam(value = "1 = QTL workflow group has Rejected status")
			@QueryParam("status_QTL_Rejected") Integer status_QTL_Rejected,
			
			@ApiParam(value = "1 = QTL workflow group has Routed status")
			@QueryParam("status_QTL_Routed") Integer status_QTL_Routed,
			
			@ApiParam(value = "1 = Tumor workflow group has Chosen status")
			@QueryParam("status_Tumor_Chosen") Integer status_Tumor_Chosen,
			
			@ApiParam(value = "1 = Tumor workflow group has Fully curated status")
			@QueryParam("status_Tumor_Full_coded") Integer status_Tumor_Full_coded,
			
			@ApiParam(value = "1 = Tumor workflow group has Indexed status")
			@QueryParam("status_Tumor_Indexed") Integer status_Tumor_Indexed,
			
			@ApiParam(value = "1 = Tumor workflow group has Not Routed status")
			@QueryParam("status_Tumor_Not_Routed") Integer status_Tumor_Not_Routed,
			
			@ApiParam(value = "1 = Tumor workflow group has Rejected status")
			@QueryParam("status_Tumor_Rejected") Integer status_Tumor_Rejected,
			
			@ApiParam(value = "1 = Tumor workflow group has Routed status")
			@QueryParam("status_Tumor_Routed") Integer status_Tumor_Routed
			);

	@GET
	@Path("/{refsKey}")
	@ApiOperation(value = "Value: Retrieve a single Reference by reference key")
	public SearchResults<ReferenceDomain> getReferenceByKey (
			@ApiParam(value = "Value: This is for searching by reference key")
			@PathParam("refsKey") String refsKey);

	@GET
	@Path("/statusHistory/{refsKey}")
	@ApiOperation(value = "Value: Retrieve the status history for a Reference by reference key")
	public SearchResults<ReferenceWorkflowStatus> getStatusHistoryByKey (
			@ApiParam(value = "Value: This identifies the desired reference key")
			@PathParam("refsKey") String refsKey);

	@DELETE
	@ApiOperation(value = "Value: Deletes Reference", notes="Notes: Deletes a Reference")
	@Path("/{id}")
	public Reference deleteReference(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			
			@ApiParam(value = "Value: This Accession ID will lookup a Reference and then delete it")
			@PathParam("id") String id
	);
}
