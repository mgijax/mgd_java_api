package org.jax.mgi.mgd.api.model.prb.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.domain.AntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntigenService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/source")
@Api(value = "Source Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntigenController extends BaseController<AntigenDomain> {

	@Inject
	private AntigenService antigenService;
	
	@Override
	public SearchResults<AntigenDomain> create(AntigenDomain domain, User user) {
		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		results = antigenService.create(domain, user);
		results = antigenService.getResults(Integer.valueOf(results.items.get(0).getAntigenKey()));
		return results;
	}

	@Override
	public SearchResults<AntigenDomain> update(AntigenDomain domain, User user) {
		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		results = antigenService.update(domain, user);
		results = antigenService.getResults(Integer.valueOf(results.items.get(0).getAntigenKey()));
		return results;
	}

	@Override
	public AntigenDomain get(Integer key) {
		return antigenService.get(key);
	}

	@Override
	public SearchResults<AntigenDomain> delete(Integer key, User user) {
		return antigenService.delete(key, user);
	}
		
}
