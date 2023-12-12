package org.jax.mgi.mgd.api.model.acc.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimMGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.service.MGITypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgitype")
@Tag(name = "MGI Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGITypeController extends BaseController<MGITypeDomain> {

	@Inject
	private MGITypeService mgitypeService;

	@Override
	public SearchResults<MGITypeDomain> create(MGITypeDomain mgitype, User user) {
		return mgitypeService.create(mgitype, user);
	}

	@Override
	public SearchResults<MGITypeDomain> update(MGITypeDomain mgitype, User user) {
		return mgitypeService.update(mgitype, user);
	}
	
	@Override
	public MGITypeDomain get(Integer key) {
		return mgitypeService.get(key);
	}

	@Override
	public SearchResults<MGITypeDomain> delete(Integer key, User user) {
		return mgitypeService.delete(key, user);
	}

	@POST
	@Operation(description = "Search")
	@Path("/search")	
	public List<SlimMGITypeDomain> search(SlimMGITypeDomain searchDomain) {
		return mgitypeService.search(searchDomain);
	}
		
}
