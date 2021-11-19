package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTSampleService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/htsample")
@Api(value = "HT Sample Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTSampleController extends BaseController<HTSampleDomain> {

	@Inject
	private HTSampleService htSampleService;

	@Override
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results = htSampleService.create(domain, user);
		results = htSampleService.getResults(Integer.valueOf(results.items.get(0).get_sample_key()));
		return results;
	}

	@Override
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results = htSampleService.update(domain, user);
		results = htSampleService.getResults(Integer.valueOf(results.items.get(0).get_sample_key()));
		return results;
	}

	@Override
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		return htSampleService.delete(key, user);
	}
	
	@Override
	public HTSampleDomain get(Integer key) {
		return htSampleService.get(key);
	}

//	@GET
//	@ApiOperation(value = "Get the object count from gxd_antibody table")
//	@Path("/getObjectCount")
//	public SearchResults<HTSampleDomain> getObjectCount() {
//		return htSampleService.getObjectCount();
//	}
		
}
