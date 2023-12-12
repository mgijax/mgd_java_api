package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;
import org.jax.mgi.mgd.api.model.mrk.service.ChromosomeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/markerChromosome")
@Tag(name = "Marker Chromosome Endpoints")
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
	@Operation(description = "Search")
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
