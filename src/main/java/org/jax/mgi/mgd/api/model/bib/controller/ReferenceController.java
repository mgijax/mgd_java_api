package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBulkDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceSearchDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceIndexDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SummaryReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reference")
@Tag(name = "Reference Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReferenceController extends BaseController<ReferenceDomain> {

	@Inject
	private ReferenceService referenceService;
	@Inject
	private UserService userService;
	
	@Override
	public SearchResults<ReferenceDomain> create(ReferenceDomain domain, User user) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results = referenceService.create(domain, user);
		results = referenceService.getResults(Integer.valueOf(results.items.get(0).getRefsKey()));
		return results;	
	}

	@Override
	public SearchResults<ReferenceDomain> update(ReferenceDomain domain, User user) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results = referenceService.update(domain, user);
		results = referenceService.getResults(Integer.valueOf(results.items.get(0).getRefsKey()));
		return results;		
	}

	@Override
	public ReferenceDomain get(Integer key) {
		return referenceService.get(key);
	}

	@Override
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		return referenceService.delete(key, user);
	}
	
	@PUT
	@Path("/bulkUpdate")
	@Operation(description = "Value: Update list of References en masse")
	public SearchResults<String> updateReferencesBulk(
			@Parameter(description = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			@Parameter(description = "Name: Logged-in User")
			@HeaderParam("username") String username,
			@Parameter(description = "Value: reference keys and data to be updated")
			ReferenceBulkDomain input
	) {
		SearchResults<String> results = new SearchResults<String>();

		if (!authenticateToken(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User user = userService.getUserByUsername(username);
		if (user != null) {
			try {
				referenceService.updateReferencesBulk(input.refsKeys, input.workflowTag, input.workflow_tag_operation, user);
			} catch (Exception e) {
				Throwable t = getRootException(e);
				String message = "\n\nBULK UPDATE FAILED.\n\n" + t.toString();
				results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);	
			}
		} else {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED);
		}
		return results;
	}

	@PUT
	@Path("/statusUpdate")
	@Operation(description = "Value: Update the status of a reference/workflow group pair")
	public SearchResults<String> updateReferenceStatus(
			@Parameter(description = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			@Parameter(description = "Name: Logged-in User")
			@HeaderParam("username") String username,
			@Parameter(description = "Value: comma-delimited list of accession IDs of references for which to set the status")
			@QueryParam("accid") String accid,
			@Parameter(description = "Value: abbreviation of workflow group for which to set the status")
			@QueryParam("group") String group,
			@Parameter(description = "Value: status term to set for the given reference/workflow group pair")
			@QueryParam("status") String status
	) {
		SearchResults<String> results = new SearchResults<String>();

		if (!authenticateToken(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User user = userService.getUserByUsername(username);
		if (user != null) {
			try {
				results = referenceService.updateReferenceStatus(api_access_token, username, accid, group, status, user);
			} catch (Exception e) {
				Throwable t = getRootException(e);
				String message = "\n\nUPDATE STATUS UPDATE FAILED.\n\n" + t.toString();
				results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);	
			}
		} else {
			results.setError("FailedAuthentication", "Failed - invalid username", Constants.HTTP_PERMISSION_DENIED);
		}
		return results;
	}
	
	@POST
	@Operation(description = "Search/returns slim reference domain")
	@Path("/search")
	public List<SlimReferenceDomain> search(ReferenceSearchDomain searchDomain) {
		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();

		try {
			results = referenceService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "get list of journals")
	@Path("/getJournalList")
	public SearchResults<String> getJournalList() {
		SearchResults<String> results = null;

		try {
			results = referenceService.getJournalList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//log.info(results);
		return results;
	}
	
	@GET
	@Operation(description = "Validate reference by J:/returns slim reference domain")
	@Path("/validJnum/{jnum}")
	public List<SlimReferenceDomain> validJnum(
			@PathParam("jnum") 
			@Parameter(description = "Validating jnum") 
			String jnum) {
		return referenceService.validJnum(jnum);
	}

	@GET
	@Operation(description = "Validate reference by J:/returns slim reference index domain")
	@Path("/validJnumGxdIndex/{jnum}")
	public List<SlimReferenceIndexDomain> validJnumGxdIndex(
			@PathParam("jnum") 
			@Parameter(description = "Validating jnum/index") 
			String jnum) {
		return referenceService.validJnumGxdIndex(jnum);
	}
	
	@POST
	@Operation(description = "Validate reference, copyright, creative commons")
	@Path("/validateJnumImage")
	public List<SlimReferenceDomain> validateJnumImage(SlimReferenceDomain domain) {
		return referenceService.validateJnumImage(domain);
	}
	
	// -----------------------------------------------------
	// get reference by allele

	@GET
	@Operation(description = "Get list of reference domains by allele accession id")
	@Path("/getRefByAllele")
	public SearchResults<SummaryReferenceDomain> getRefByAllele(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SummaryReferenceDomain> results = new SearchResults<SummaryReferenceDomain>();

		try {
			results = referenceService.getRefByAllele(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadRefByAllele")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadRefByAllele(@QueryParam("accid") String accid) {
             return referenceService.downloadRefByAllele(accid);
	}
	
	// -----------------------------------------------------
	// get reference by marker

	@GET
	@Operation(description = "Get list of reference domains by marker accession id")
	@Path("/getRefByMarker")
	public SearchResults<SummaryReferenceDomain> getRefByMarker(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SummaryReferenceDomain> results = new SearchResults<SummaryReferenceDomain>();

		try {
			results = referenceService.getRefByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadRefByMarker")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadRefByMarker(@QueryParam("accid") String accid) {
             return referenceService.downloadRefByMarker(accid);
	}
	
	// -----------------------------------------------------
	// get reference by search

	@POST
	@Operation(description = "Get list of reference domains by search domain")
	@Path("/getRefBySearch")
	public SearchResults<SummaryReferenceDomain> getRefBySearch(SummaryReferenceDomain searchDomain) {
		
		SearchResults<SummaryReferenceDomain> results = new SearchResults<SummaryReferenceDomain>();

		try {
			results = referenceService.getRefBySearch(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadRefBySearch")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadRefBySearch(
		@QueryParam("accID") String accID,
		@QueryParam("authors") String authors,
		@QueryParam("primaryAuthor") String primaryAuthor,
		@QueryParam("title") String title,
		@QueryParam("journal") String journal,
		@QueryParam("vol") String vol,
		@QueryParam("year") String year
	) {
	     SummaryReferenceDomain searchDomain = new SummaryReferenceDomain();
	     searchDomain.setAccID(accID);
	     searchDomain.setAuthors(authors);
	     searchDomain.setPrimaryAuthor(primaryAuthor);
	     searchDomain.setTitle(title);
	     searchDomain.setJournal(journal);
	     searchDomain.setVol(vol);
	     searchDomain.setYear(year);
             return referenceService.downloadRefBySearch(searchDomain);
	}
}
