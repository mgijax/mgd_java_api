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
import org.jax.mgi.mgd.api.model.gxd.domain.AntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntigenService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/antigen")
@Api(value = "Antigen Endpoints")
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
	public SearchResults<AntigenDomain> delete(Integer key, User user) {
		return antigenService.delete(key, user);
	}
	
	@Override
	public AntigenDomain get(Integer key) {
		return antigenService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from gxd_antigen table")
	@Path("/getObjectCount")
	public SearchResults<AntigenDomain> getObjectCount() {
		return antigenService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns antigen domain")
	@Path("/search")
	public List<SlimAntigenDomain> search(AntigenDomain searchDomain) {
	
		List<SlimAntigenDomain> results = new ArrayList<SlimAntigenDomain>();

		try {
			results = antigenService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
