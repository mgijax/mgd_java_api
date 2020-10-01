package org.jax.mgi.mgd.api.model.prb.controller;

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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/probe")
@Api(value = "Probe Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeController extends BaseController<ProbeDomain> {

	@Inject
	private ProbeService probeService;

	@Override
	public SearchResults<ProbeDomain> create(ProbeDomain probe, User user) {
		return probeService.create(probe, user);
	}

	@Override
	public SearchResults<ProbeDomain> update(ProbeDomain probe, User user) {
		return probeService.update(probe, user);
	}

	@Override
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		return probeService.delete(key, user);
	}
		
	@Override
	public ProbeDomain get(Integer key) {
		return probeService.get(key);
	}


	@GET
	@ApiOperation(value = "Get the object count from prb_probe table")
	@Path("/getObjectCount")
	public SearchResults<ProbeDomain> getObjectCount() {
		return probeService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns antigen domain")
	@Path("/search")
	public List<SlimProbeDomain> search(ProbeDomain searchDomain) {
	
		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();

		try {
			results = probeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
