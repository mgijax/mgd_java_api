package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/mgireferenceassoc")
@Api(value = "MGI Reference Assoc Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGIReferenceAssocController extends BaseController<MGIReferenceAssocDomain> {

	@Inject
	private MGIReferenceAssocService referenceAssocService;

	@Override
	public SearchResults<MGIReferenceAssocDomain> create(MGIReferenceAssocDomain domain, User user) {
		return referenceAssocService.create(domain, user);
	}

	@Override
	public SearchResults<MGIReferenceAssocDomain> update(MGIReferenceAssocDomain domain, User user) {
		return referenceAssocService.update(domain, user);
	}

	@Override
	public MGIReferenceAssocDomain get(Integer key) {
		return referenceAssocService.get(key);
	}

	@Override
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		return referenceAssocService.delete(key, user);
	}
	
	@GET
	@ApiOperation(value = "Get All Reference Associations by Marker")
	@Path("/marker/{key}")
	public List<MGIReferenceAssocDomain> getByMarker(@PathParam("key") @ApiParam(value = "Marker key associated w/ references") Integer key) {
			
		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();
		
		try {
			results = referenceAssocService.getByMarker(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public void process(String parentKey, List<MGIReferenceAssocDomain> domain, String mgiTypeKey, User user) {
		referenceAssocService.process(parentKey, domain, mgiTypeKey, user);
		return;
	}
	
}
