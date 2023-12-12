package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/note")
@Tag(name = "Note Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteController extends BaseController<NoteDomain> {

	@Inject
	private NoteService noteService;

	@Override
	public SearchResults<NoteDomain> create(NoteDomain note, User user) {
		return noteService.create(note, user);
	}

	@Override
	public SearchResults<NoteDomain> update(NoteDomain note, User user) {
		return noteService.update(note, user);
	}

	@Override
	public NoteDomain get(Integer key) {
		return noteService.get(key);
	}

	@Override
	public SearchResults<NoteDomain> delete(Integer key, User user) {
		return noteService.delete(key, user);
	}

	@POST
	@Operation(description = "Get All Notes by Marker")
	@Path("/marker")
	public List<NoteDomain> getByMarker(Integer key) {
			
		List<NoteDomain> results = new ArrayList<NoteDomain>();
		
		try {
			results = noteService.getByMarker(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}
