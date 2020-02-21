package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AssayNoteService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/assaynote")
@Api(value = "Assay Note Endpoints")
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
