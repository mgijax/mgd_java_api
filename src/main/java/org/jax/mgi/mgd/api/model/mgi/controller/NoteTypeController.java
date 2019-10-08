package org.jax.mgi.mgd.api.model.mgi.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/noteType")
@Api(value = "Note Type Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public SearchResults<NoteTypeDomain> search(NoteTypeDomain searchDomain) {
		return noteTypeService.search(searchDomain);
	}
	
}
