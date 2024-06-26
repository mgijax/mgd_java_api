package org.jax.mgi.mgd.api.model.gxd.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTExperimentVariableService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/htexperimentvariable")
@Tag(name = "HT Experiment Variable Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTExperimentVariableController extends BaseController<HTExperimentVariableDomain> {

	@Inject
	private HTExperimentVariableService htExperimentVariableService; 

	@Override
	public SearchResults<HTExperimentVariableDomain> create(HTExperimentVariableDomain domain, User user) {
		SearchResults<HTExperimentVariableDomain> results = new SearchResults<HTExperimentVariableDomain>();
		results = htExperimentVariableService.create(domain, user);
		results = htExperimentVariableService.getResults(Integer.valueOf(results.items.get(0).get_experimentvariable_key()));
		return results;
	}

	@Override
	public SearchResults<HTExperimentVariableDomain> update(HTExperimentVariableDomain domain, User user) {
		SearchResults<HTExperimentVariableDomain> results = new SearchResults<HTExperimentVariableDomain>();
		results = htExperimentVariableService.update(domain, user);
		results = htExperimentVariableService.getResults(Integer.valueOf(results.items.get(0).get_experimentvariable_key()));
		return results;
	}

	@Override
	public SearchResults<HTExperimentVariableDomain> delete(Integer key, User user) {
		return htExperimentVariableService.delete(key, user);
	}
	
	@Override
	public HTExperimentVariableDomain get(Integer key) {
		return htExperimentVariableService.get(key);
	}
	
}
