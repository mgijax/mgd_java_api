package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTExperimentService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ht")
@Tag(name = "HT Experiment Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTExperimentController extends BaseController<HTDomain> {

	@Inject
	private HTExperimentService htExperimentService;

	@Override
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		results = htExperimentService.create(domain, user);
		results = htExperimentService.getResults(results.items.get(0).get_experiment_key());		
		return results;
	}

	@Override
	public SearchResults<HTDomain> update(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		results = htExperimentService.update(domain, user);
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
	
	@GET
	@Operation(description = "Get the object count from gxd_assay table")
	@Path("/getObjectCount")
	public SearchResults<HTDomain> getObjectCount() {
		return htExperimentService.getObjectCount();
	}
	
	@POST
	@Operation(description = "Search returns HT domain")
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
