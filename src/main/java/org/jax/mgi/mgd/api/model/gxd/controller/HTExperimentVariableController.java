package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTExperimentVariableService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/htexperimentvariable")
@Api(value = "HT Experiment Variable Endpoints")
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
