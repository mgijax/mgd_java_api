package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISetService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgiset")
@Api(value = "MGI Set Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISetController extends BaseController<MGISetDomain> {

	@Inject
	private MGISetService setService;

	@Override
	public SearchResults<MGISetDomain> create(MGISetDomain domain, User user) {
		return setService.create(domain, user);
	}

	@Override
	public SearchResults<MGISetDomain> update(MGISetDomain domain, User user) {
		return setService.update(domain, user);
	}

	@Override
	public MGISetDomain get(Integer key) {
		return setService.get(key);
	}

	@Override
	public SearchResults<MGISetDomain> delete(Integer key, User user) {
		return setService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Get Set Members by Set/User")
	@Path("/getBySetUser")
	public List<MGISetDomain> getBySetUser(MGISetDomain domain) {
			
		List<MGISetDomain> results = new ArrayList<MGISetDomain>();
		
		try {
			results = setService.getBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
