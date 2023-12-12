package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgiSynonymType")
@Tag(name = "MGI Synonym Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISynonymTypeController extends BaseController<MGISynonymTypeDomain> {

	@Inject
	private MGISynonymTypeService synonymTypeService;

	@Override
	public SearchResults<MGISynonymTypeDomain> create(MGISynonymTypeDomain event, User user) {
		return synonymTypeService.create(event, user);
	}

	@Override
	public SearchResults<MGISynonymTypeDomain> update(MGISynonymTypeDomain event, User user) {
		return synonymTypeService.update(event, user);
	}

	@Override
	public MGISynonymTypeDomain get(Integer key) {
		return synonymTypeService.get(key);
	}

	@Override
	public SearchResults<MGISynonymTypeDomain> delete(Integer key, User user) {
		return synonymTypeService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<MGISynonymTypeDomain> search(MGISynonymTypeDomain searchDomain) {
		return synonymTypeService.search(searchDomain);
	}
	
}
