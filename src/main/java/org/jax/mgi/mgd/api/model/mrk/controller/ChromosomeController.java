package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;
import org.jax.mgi.mgd.api.model.mrk.service.ChromosomeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerChromosome")
@Api(value = "Marker Chromosome Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChromosomeController extends BaseController<ChromosomeDomain> {

	@Inject
	private ChromosomeService chromosomeService;

	@Override
	public SearchResults<ChromosomeDomain> create(ChromosomeDomain event, User user) {
		return chromosomeService.create(event, user);
	}

	@Override
	public SearchResults<ChromosomeDomain> update(ChromosomeDomain event, User user) {
		return chromosomeService.update(event, user);
	}

	@Override
	public ChromosomeDomain get(Integer key) {
		return chromosomeService.get(key);
	}

	@Override
	public SearchResults<ChromosomeDomain> delete(Integer key, User user) {
		return chromosomeService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<ChromosomeDomain> search(ChromosomeDomain searchDomain) {
			
		List<ChromosomeDomain> results = new ArrayList<ChromosomeDomain>();
		
		try {
			results = chromosomeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
