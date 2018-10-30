package org.jax.mgi.mgd.api.model.prb.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.search.ProbeSearchForm;
import org.jax.mgi.mgd.api.model.prb.service.ProbeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/probe")
@Api(value = "Probe Endpoints", description="CRUD operations for probes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeController extends BaseController<ProbeDomain> implements BaseSearchInterface<ProbeDomain, ProbeSearchForm> {

	@Inject
	private ProbeService probeService;

	public ProbeDomain create(ProbeDomain probe, User user) {
		try {
			return probeService.create(probe, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ProbeDomain update(ProbeDomain probe, User user) {
		return probeService.update(probe, user);
	}

	public ProbeDomain get(Integer key) {
		return probeService.get(key);
	}

	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		return probeService.delete(key, user);
	}
	
	@Override
	public SearchResults<ProbeDomain> search(ProbeSearchForm searchForm) {
		return probeService.search(searchForm);
	}

}
