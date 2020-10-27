package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GelControlDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GelControlService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/gelcontrol")
@Api(value = "Gel Control Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GelControlController extends BaseController<GelControlDomain> {

	@Inject
	private GelControlService gelControlService;

	@Override
	public SearchResults<GelControlDomain> create(GelControlDomain domain, User user) {
		SearchResults<GelControlDomain> results = new SearchResults<GelControlDomain>();
		results = gelControlService.create(domain, user);
		results = gelControlService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<GelControlDomain> update(GelControlDomain domain, User user) {
		SearchResults<GelControlDomain> results = new SearchResults<GelControlDomain>();
		results = gelControlService.update(domain, user);
		results.setItems(gelControlService.search(domain));
		return results;
	}

	@Override
	public SearchResults<GelControlDomain> delete(Integer key, User user) {
		return gelControlService.delete(key, user);
	}
	
	@Override
	public GelControlDomain get(Integer key) {
		return gelControlService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<GelControlDomain> search(GelControlDomain searchDomain) {
	
		List<GelControlDomain> results = new ArrayList<GelControlDomain>();

		try {
			results = gelControlService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
