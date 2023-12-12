package org.jax.mgi.mgd.api.model.mgi.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/noteType")
@Tag(name = "Note Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteTypeController extends BaseController<NoteTypeDomain> {

	@Inject
	private NoteTypeService noteTypeService;

	@Override
	public SearchResults<NoteTypeDomain> create(NoteTypeDomain event, User user) {
		return noteTypeService.create(event, user);
	}

	@Override
	public SearchResults<NoteTypeDomain> update(NoteTypeDomain event, User user) {
		return noteTypeService.update(event, user);
	}

	@Override
	public NoteTypeDomain get(Integer key) {
		return noteTypeService.get(key);
	}

	@Override
	public SearchResults<NoteTypeDomain> delete(Integer key, User user) {
		return noteTypeService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public SearchResults<NoteTypeDomain> search(NoteTypeDomain searchDomain) {
		return noteTypeService.search(searchDomain);
	}
	
}
