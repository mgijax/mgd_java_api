package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTExperimentService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/ht")
@Api(value = "HT Experiment Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTExperimentController extends BaseController<HTDomain> {

	@Inject
	private HTExperimentService htExperimentService;

	@Override
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();

		try {
			results = htExperimentService.create(domain, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		results = htExperimentService.getResults(results.items.get(0).get_experiment_key());		
		return results;
	}

	@Override
	public SearchResults<HTDomain> update(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();

		try {
			results = htExperimentService.update(domain, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		results = htExperimentService.getResults(results.items.get(0).get_experiment_key());		
		return results;
	}
	
	@Override
	public SearchResults<HTDomain> delete(Integer key, User user) {
		return htExperimentService.delete(key, user);
	}	

	@Override
	public HTDomain get(Integer key) {
		return htExperimentService.get(key);
	}
	
	@POST
	@ApiOperation(value = "Search returns HT domain")
	@Path("/search")
	public List<SlimHTDomain> search(HTDomain searchDomain) {
	
		List<SlimHTDomain> results = new ArrayList<SlimHTDomain>();

		try {
			results = htExperimentService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	
}
