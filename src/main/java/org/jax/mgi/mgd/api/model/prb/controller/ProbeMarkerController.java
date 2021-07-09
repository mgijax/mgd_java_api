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
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeMarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/probemarker")
@Api(value = "Probe Marker Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeMarkerController extends BaseController<ProbeMarkerDomain> {

	@Inject
	private ProbeMarkerService probeMarkerService;

	@Override
	public SearchResults<ProbeMarkerDomain> create(ProbeMarkerDomain domain, User user) {
		SearchResults<ProbeMarkerDomain> results = new SearchResults<ProbeMarkerDomain>();
		results = probeMarkerService.create(domain, user);
		results = probeMarkerService.getResults(Integer.valueOf(results.items.get(0).getProbeKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeMarkerDomain> update(ProbeMarkerDomain domain, User user) {
		SearchResults<ProbeMarkerDomain> results = new SearchResults<ProbeMarkerDomain>();
		results = probeMarkerService.update(domain, user);
		results = probeMarkerService.getResults(Integer.valueOf(results.items.get(0).getProbeKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeMarkerDomain> delete(Integer key, User user) {
		return probeMarkerService.delete(key, user);
	}
	
	@Override
	public ProbeMarkerDomain get(Integer key) {
		return probeMarkerService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns probe marker domain")
	@Path("/search")
	public List<ProbeMarkerDomain> search(ProbeMarkerDomain searchDomain) {
	
		List<ProbeMarkerDomain> results = new ArrayList<ProbeMarkerDomain>();

		try {
			results = probeMarkerService.search(Integer.valueOf(searchDomain.getProbeKey()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@POST
	@ApiOperation(value = "Validate Probe/Marker, returns List of ProbeMarkerDomain")
	@Path("/validateProbeyMarker")
	public List<ProbeMarkerDomain> validateProbeMarker(ProbeMarkerDomain searchDomain) {
	
		List<ProbeMarkerDomain> results = new ArrayList<ProbeMarkerDomain>();

		try {
			results = probeMarkerService.validateProbeMarker(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
