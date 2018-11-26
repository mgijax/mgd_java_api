package org.jax.mgi.mgd.api.model.mgi.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/mgireferenceassoc")
@Api(value = "MGI Reference Assoc Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGIReferenceAssocController extends BaseController<MGIReferenceAssocDomain> {

	@Inject
	private MGIReferenceAssocService referenceAssocService;

	@Override
	public SearchResults<MGIReferenceAssocDomain> create(MGIReferenceAssocDomain refAssoc, User user) {
		return referenceAssocService.create(refAssoc, user);
	}

	@Override
	public SearchResults<MGIReferenceAssocDomain> update(MGIReferenceAssocDomain refAssoc, User user) {
		return referenceAssocService.update(refAssoc, user);
	}

	@Override
	public MGIReferenceAssocDomain get(Integer key) {
		return referenceAssocService.get(key);
	}

	@Override
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		return referenceAssocService.delete(key, user);
	}
	
}
