package org.jax.mgi.mgd.api.model.gxd.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AssayNoteService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/assaynote")
@Tag(name = "Assay Note Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssayNoteController extends BaseController<AssayNoteDomain> {

	@Inject
	private AssayNoteService assayNoteService;

	@Override
	public SearchResults<AssayNoteDomain> create(AssayNoteDomain domain, User user) {
		SearchResults<AssayNoteDomain> results = new SearchResults<AssayNoteDomain>();
		results = assayNoteService.create(domain, user);
		results = assayNoteService.getResults(Integer.valueOf(results.items.get(0).getAssayNoteKey()));
		return results;
	}

	@Override
	public SearchResults<AssayNoteDomain> update(AssayNoteDomain domain, User user) {
		SearchResults<AssayNoteDomain> results = new SearchResults<AssayNoteDomain>();
		results = assayNoteService.update(domain, user);
		results = assayNoteService.getResults(Integer.valueOf(results.items.get(0).getAssayNoteKey()));
		return results;
	}

	@Override
	public SearchResults<AssayNoteDomain> delete(Integer key, User user) {
		return assayNoteService.delete(key, user);
	}
	
	@Override
	public AssayNoteDomain get(Integer key) {
		return assayNoteService.get(key);
	}
	
}
