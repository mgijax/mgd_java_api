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
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/htsample")
@Api(value = "HT Sample Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTSampleController extends BaseController<HTDomain> {

	@Inject
	private HTService HTService;

	@Override
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//		results = HTService.create(domain, user);
//		results = HTService.getResults(Integer.valueOf(results.items.get(0).getHTKey()));
		return results;
	}

	@Override
	public SearchResults<HTDomain> update(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//		results = HTService.update(domain, user);
//		results = HTService.getResults(Integer.valueOf(results.items.get(0).getHTKey()));
		return results;
	}

	@Override
	public SearchResults<HTDomain> delete(Integer key, User user) {
		return HTService.delete(key, user);
	}
	
	@Override
	public HTDomain get(Integer key) {
		return HTService.get(key);
	}
	
}
