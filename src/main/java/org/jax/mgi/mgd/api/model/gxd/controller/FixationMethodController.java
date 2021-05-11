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
import org.jax.mgi.mgd.api.model.gxd.domain.FixationMethodDomain;
import org.jax.mgi.mgd.api.model.gxd.service.FixationMethodService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/gxdfixation")
@Api(value = "GXD FixationMethod Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FixationMethodController extends BaseController<FixationMethodDomain> {

	@Inject
	private FixationMethodService fixationService;

	@Override
	public SearchResults<FixationMethodDomain> create(FixationMethodDomain domain, User user) {
		SearchResults<FixationMethodDomain> results = new SearchResults<FixationMethodDomain>();
		results = fixationService.create(domain, user);
		results = fixationService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<FixationMethodDomain> update(FixationMethodDomain domain, User user) {
		SearchResults<FixationMethodDomain> results = new SearchResults<FixationMethodDomain>();
		results = fixationService.update(domain, user);
		return results;
	}

	@Override
	public SearchResults<FixationMethodDomain> delete(Integer key, User user) {
		return fixationService.delete(key, user);
	}
	
	@Override
	public FixationMethodDomain get(Integer key) {
		return fixationService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<FixationMethodDomain> search(FixationMethodDomain searchDomain) {
	
		List<FixationMethodDomain> results = new ArrayList<FixationMethodDomain>();

		try {
			results = fixationService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
