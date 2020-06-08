package org.jax.mgi.mgd.api.model.all.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleMutationDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleMutationService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/allelemutation")
@Api(value = "Allele Mutation Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleMutationController extends BaseController<AlleleMutationDomain> {

	@Inject
	private AlleleMutationService alleleMutationService;

	@Override
	public SearchResults<AlleleMutationDomain> create(AlleleMutationDomain domain, User user) {
		SearchResults<AlleleMutationDomain> results = new SearchResults<AlleleMutationDomain>();
		results = alleleMutationService.create(domain, user);
		results = alleleMutationService.getResults(Integer.valueOf(results.items.get(0).getAssocKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleMutationDomain> update(AlleleMutationDomain domain, User user) {
		SearchResults<AlleleMutationDomain> results = new SearchResults<AlleleMutationDomain>();
		results = alleleMutationService.update(domain, user);
		results = alleleMutationService.getResults(Integer.valueOf(results.items.get(0).getAssocKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleMutationDomain> delete(Integer key, User user) {
		return alleleMutationService.delete(key, user);
	}
	
	@Override
	public AlleleMutationDomain get(Integer key) {
		return alleleMutationService.get(key);
	}
	
}
