package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBulkDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceSearchDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.UserService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/reference")
@Api(value = "Reference Endpoints")
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
	@ApiOperation(value = "Value: Update list of References en masse")
	public SearchResults<String> updateReferencesBulk(
			@ApiParam(value = "Name: Token for accessing this API")
			@HeaderParam("api_access_token") String api_access_token,
			@ApiParam(value = "Name: Logged-in User")
			@HeaderParam("username") String username,
			@ApiParam(value = "Value: reference keys and data to be updated")
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
	@ApiOperation(value = "Value: Update the status of a reference/workflow group pair")
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
	) {
		SearchResults<String> results = new SearchResults<String>();

		if (!authenticateToken(api_access_token)) {
			results.setError("FailedAuthentication", "Failed - invalid api_access_token", Constants.HTTP_PERMISSION_DENIED);
			return results;
		}

		User user = userService.getUserByUsername(username);
		if (user != null) {
			try {
				referenceService.updateReferenceStatus(api_access_token, username, accid, group, status, user);
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
	@ApiOperation(value = "Search/returns slim reference domain")
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
	@ApiOperation(value = "get list of journals")
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
	@ApiOperation(value = "Validate reference by J:/returns slim reference domain")
	@Path("/validJnum/{jnum}")
	public List<SlimReferenceDomain> validJnum(
			@PathParam("jnum") 
			@ApiParam(value = "Validating jnum") 
			String jnum) {
		return referenceService.validJnum(jnum);
	}

	@POST
	@ApiOperation(value = "Validate reference, copyright, creative commons")
	@Path("/validateJnumImage")
	public List<SlimReferenceDomain> validateJnumImage(SlimReferenceDomain domain) {
		return referenceService.validateJnumImage(domain);
	}

}
