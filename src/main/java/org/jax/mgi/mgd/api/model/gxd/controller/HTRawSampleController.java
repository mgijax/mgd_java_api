package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTRawSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTRawSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTRawSampleService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/htrawsample")
@Tag(name = "HT Raw Sample Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTRawSampleController extends BaseController<HTRawSampleDomain> {

	@Inject
	private HTRawSampleService htSampleService;

	@Override
	public SearchResults<HTRawSampleDomain> create(HTRawSampleDomain domain, User user) {
		SearchResults<HTRawSampleDomain> results = new SearchResults<HTRawSampleDomain>();
		results = htSampleService.create(domain, user);
		results = htSampleService.getResults(Integer.valueOf(results.items.get(0).get_rawsample_key()));
		return results;
	}

	@Override
	public SearchResults<HTRawSampleDomain> update(HTRawSampleDomain domain, User user) {
		SearchResults<HTRawSampleDomain> results = new SearchResults<HTRawSampleDomain>();
		results = htSampleService.update(domain, user);
		results = htSampleService.getResults(Integer.valueOf(results.items.get(0).get_rawsample_key()));
		return results;
	}

	@Override
	public SearchResults<HTRawSampleDomain> delete(Integer key, User user) {
		return htSampleService.delete(key, user);
	}

	@Override
	public HTRawSampleDomain get(Integer key) {
		return htSampleService.get(key);
	}
	
	@POST
	@Operation(description = "Search returns list of raw sample domains")
	@Path("/search")
	public List<SlimHTRawSampleDomain> search(HTDomain searchDomain) {
		log.info("HT Raw Sample search controller");

		List<SlimHTRawSampleDomain> results = new ArrayList<SlimHTRawSampleDomain>();

		try {
			results = htSampleService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}
