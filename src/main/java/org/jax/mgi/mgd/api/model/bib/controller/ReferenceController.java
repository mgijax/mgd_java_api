package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/reference")
@Api(value = "Reference Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReferenceController extends BaseController<ReferenceDomain> {

	@Inject
	private ReferenceService referenceService;

	@Override
	public SearchResults<ReferenceDomain> create(ReferenceDomain reference, User user) {
		return referenceService.create(reference, user);
	}

	@Override
	public SearchResults<ReferenceDomain> update(ReferenceDomain reference, User user) {
		return referenceService.update(reference, user);
	}

	@Override
	public ReferenceDomain get(Integer key) {
		return referenceService.get(key);
	}

	@Override
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		return referenceService.delete(key, user);
	}
	
	@GET
	@ApiOperation(value = "Confirm reference is valid")
	@Path("/valid")
	public SearchResults<ReferenceDomain> isValid(
		@ApiParam(value = "Value: This is for searching by reference key")
		@QueryParam("refsKey") String refsKey
		) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		ReferenceDomain ref = this.get(Integer.parseInt(refsKey));
		if (ref != null) {
			results.setItem(ref);
		} else {
			results.setError("InvalidReference", "No reference with key " + refsKey + " exists.", Constants.HTTP_NOT_FOUND);
		}
		return results;
	}

	@POST
	@ApiOperation(value = "Validate reference by J:/returns slim reference domain")
	@Path("/validJnum")
	public List<SlimReferenceDomain> validJnum(String value) {
		return referenceService.validJnum(value);
	}
		
}
