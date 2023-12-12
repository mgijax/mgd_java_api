package org.jax.mgi.mgd.api.model.mgi.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIRefAssocTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIRefAssocTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgiRefAssocType")
@Tag(name = "MGI Referene Assoc Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGIRefAssocTypeController extends BaseController<MGIRefAssocTypeDomain> {

	@Inject
	private MGIRefAssocTypeService refAssocTypeService;

	@Override
	public SearchResults<MGIRefAssocTypeDomain> create(MGIRefAssocTypeDomain event, User user) {
		return refAssocTypeService.create(event, user);
	}

	@Override
	public SearchResults<MGIRefAssocTypeDomain> update(MGIRefAssocTypeDomain event, User user) {
		return refAssocTypeService.update(event, user);
	}

	@Override
	public MGIRefAssocTypeDomain get(Integer key) {
		return refAssocTypeService.get(key);
	}

	@Override
	public SearchResults<MGIRefAssocTypeDomain> delete(Integer key, User user) {
		return refAssocTypeService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public SearchResults<MGIRefAssocTypeDomain> search(MGIRefAssocTypeDomain searchDomain) {
		return refAssocTypeService.search(searchDomain);
	}
	
}
