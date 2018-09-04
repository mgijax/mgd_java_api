package org.jax.mgi.mgd.api.model.mgi.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.NoteSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/organism")
@Api(value = "Note Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteController extends BaseController<NoteDomain> implements BaseSearchInterface<NoteDomain, NoteSearchForm> {

	@Inject
	private NoteService organismService;

	public NoteDomain create(NoteDomain organism, User user) {
		try {
			return organismService.create(organism, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public NoteDomain update(NoteDomain organism, User user) {
		return organismService.update(organism, user);
	}

	public NoteDomain get(Integer key) {
		return organismService.get(key);
	}

	public NoteDomain delete(Integer organism_key, User user) {
		return organismService.delete(organism_key, user);
	}

	@Override
	public SearchResults<NoteDomain> search(NoteSearchForm searchForm) {
		return organismService.search(searchForm);
	}

}
