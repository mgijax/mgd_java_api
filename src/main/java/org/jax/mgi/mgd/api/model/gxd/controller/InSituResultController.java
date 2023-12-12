package org.jax.mgi.mgd.api.model.gxd.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultDomain;
import org.jax.mgi.mgd.api.model.gxd.service.InSituResultService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/insituresult")
@Tag(name = "InSituResult Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InSituResultController extends BaseController<InSituResultDomain> {

	@Inject
	private InSituResultService insituResultService;

	@Override
	public SearchResults<InSituResultDomain> create(InSituResultDomain domain, User user) {
		SearchResults<InSituResultDomain> results = new SearchResults<InSituResultDomain>();
		results = insituResultService.create(domain, user);
		results = insituResultService.getResults(Integer.valueOf(results.items.get(0).getResultKey()));
		return results;
	}

	@Override
	public SearchResults<InSituResultDomain> update(InSituResultDomain domain, User user) {
		SearchResults<InSituResultDomain> results = new SearchResults<InSituResultDomain>();
		results = insituResultService.update(domain, user);
		results = insituResultService.getResults(Integer.valueOf(results.items.get(0).getResultKey()));
		return results;
	}

	@Override
	public SearchResults<InSituResultDomain> delete(Integer key, User user) {
		return insituResultService.delete(key, user);
	}
	
	@Override
	public InSituResultDomain get(Integer key) {
		return insituResultService.get(key);
	}
	
}
