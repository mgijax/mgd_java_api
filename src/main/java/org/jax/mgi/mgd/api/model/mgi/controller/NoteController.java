package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.NoteSearchForm;
import org.jax.mgi.mgd.api.model.mgi.search.OrganismSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/note")
@Api(value = "Note Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteController extends BaseController<NoteDomain> {

	@Inject
	private NoteService noteService;

	public NoteDomain create(NoteDomain note, User user) {
		try {
			return noteService.create(note, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public NoteDomain update(NoteDomain note, User user) {
		return noteService.update(note, user);
	}

	public NoteDomain get(Integer key) {
		return noteService.get(key);
	}

	public SearchResults<NoteDomain> delete(Integer key, User user) {
		return noteService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<NoteDomain> search(NoteSearchForm searchForm) {
		return noteService.search(searchForm);
	}
	
}
