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
import org.jax.mgi.mgd.api.model.gxd.domain.PatternDomain;
import org.jax.mgi.mgd.api.model.gxd.service.PatternService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/gxdpattern")
@Api(value = "GXD Pattern Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatternController extends BaseController<PatternDomain> {

	@Inject
	private PatternService patternService;

	@Override
	public SearchResults<PatternDomain> create(PatternDomain domain, User user) {
		SearchResults<PatternDomain> results = new SearchResults<PatternDomain>();
		results = patternService.create(domain, user);
		results = patternService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<PatternDomain> update(PatternDomain domain, User user) {
		SearchResults<PatternDomain> results = new SearchResults<PatternDomain>();
		results = patternService.update(domain, user);
		results = patternService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<PatternDomain> delete(Integer key, User user) {
		return patternService.delete(key, user);
	}
	
	@Override
	public PatternDomain get(Integer key) {
		return patternService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<PatternDomain> search(PatternDomain searchDomain) {
	
		List<PatternDomain> results = new ArrayList<PatternDomain>();

		try {
			results = patternService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
