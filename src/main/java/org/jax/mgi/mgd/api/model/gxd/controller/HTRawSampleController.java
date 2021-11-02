package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTRawSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTRawSampleService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/htrawsample")
@Api(value = "HT Raw Sample Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTRawSampleController extends BaseController<HTRawSampleDomain> {

	@Inject
	private HTRawSampleService htSampleService;
 
	@Override
	public HTRawSampleDomain get(Integer key) {
		return htSampleService.get(key);
	}

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
		
}
