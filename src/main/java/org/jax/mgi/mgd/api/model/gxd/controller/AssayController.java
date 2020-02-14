package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AssayService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/assay")
@Api(value = "Assay Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssayController extends BaseController<AssayDomain> {

	@Inject
	private AssayService assayService;

	@Override
	public SearchResults<AssayDomain> create(AssayDomain domain, User user) {
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		results = assayService.create(domain, user);
		results = assayService.getResults(Integer.valueOf(results.items.get(0).getAssayKey()));
		return results;
	}

	@Override
	public SearchResults<AssayDomain> update(AssayDomain domain, User user) {
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		results = assayService.update(domain, user);
		results = assayService.getResults(Integer.valueOf(results.items.get(0).getAssayKey()));
		return results;
	}

	@Override
	public AssayDomain get(Integer key) {
		return assayService.get(key);
	}

	@Override
	public SearchResults<AssayDomain> delete(Integer key, User user) {
		return assayService.delete(key, user);
	}
	
}
