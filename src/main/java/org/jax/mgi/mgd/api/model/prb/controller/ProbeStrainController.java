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
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeStrainService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/strain")
@Api(value = "Strain Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeStrainController extends BaseController<ProbeStrainDomain> {

	@Inject
	private ProbeStrainService probeStrainService;

	@Override
	public SearchResults<ProbeStrainDomain> create(ProbeStrainDomain strain, User user) {
		return probeStrainService.create(strain, user);
	}

	@Override
	public SearchResults<ProbeStrainDomain> update(ProbeStrainDomain strain, User user) {
		return probeStrainService.update(strain, user);
	}

	@Override
	public ProbeStrainDomain get(Integer key) {
		return probeStrainService.get(key);
	}

	@Override
	public SearchResults<ProbeStrainDomain> delete(Integer key, User user) {
		return probeStrainService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Validate Strain")
	@Path("/validateStrain")
	public List<SlimProbeStrainDomain> validateStrain(SlimProbeStrainDomain searchDomain) {
		
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		try {
			results = probeStrainService.validateStrain(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}
