package org.jax.mgi.mgd.api.model.mgi.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIKeyValueDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIKeyValueService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/keyvalue")
@Tag(name = "MGI Key Value Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGIKeyValueController extends BaseController<MGIKeyValueDomain> {

	@Inject
	private MGIKeyValueService mgiKeyValueService;

	@Override
	public SearchResults<MGIKeyValueDomain> create(MGIKeyValueDomain mgiKeyValueDomain, User user) {
		return mgiKeyValueService.create(mgiKeyValueDomain, user);
	}

	@Override
	public SearchResults<MGIKeyValueDomain> update(MGIKeyValueDomain mgiKeyValueDomain, User user) {
		return mgiKeyValueService.update(mgiKeyValueDomain, user);
	}

	@Override
	public MGIKeyValueDomain get(Integer key) {
		return mgiKeyValueService.get(key);
	}

	@Override
	public SearchResults<MGIKeyValueDomain> delete(Integer key, User user) {
		return mgiKeyValueService.delete(key, user);
	}

}
