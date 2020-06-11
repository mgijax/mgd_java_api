package org.jax.mgi.mgd.api.model.prb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeTissueDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeTissueService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Path("/tissue")
@Api(value = "Tissue Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeTissueController extends BaseController<ProbeTissueDomain> {

	@Inject
	private ProbeTissueService probeTissueService;
	
	@Override
	public SearchResults<ProbeTissueDomain> create(ProbeTissueDomain domain, User user) {
		log.info("ProbeTissueController create");
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		results = probeTissueService.create(domain, user);
		results = probeTissueService.getResults(Integer.valueOf(results.items.get(0).getTissueKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeTissueDomain> update(ProbeTissueDomain domain, User user) {
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		results = probeTissueService.update(domain, user);
		results = probeTissueService.getResults(Integer.valueOf(results.items.get(0).getTissueKey()));
		return results;
	}

	@Override
	public ProbeTissueDomain get(Integer key) {
		return probeTissueService.get(key);
	}

	@Override
	public SearchResults<ProbeTissueDomain> delete(Integer key, User user) {
		return probeTissueService.delete(key, user);
	}
	@POST
	@ApiOperation(value = "Search/returns probe tissue domain")
	@Path("/search")
	public List<ProbeTissueDomain> search(ProbeTissueDomain searchDomain) {
	
		List<ProbeTissueDomain> results = new ArrayList<ProbeTissueDomain>();

		try {
			results = probeTissueService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}
