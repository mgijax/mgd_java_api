package org.jax.mgi.mgd.api.model.prb.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeMarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/probemarker")
@Tag(name = "Probe Marker Endpoints")
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
	@Operation(description = "Search/returns probe marker domain")
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
	@Operation(description = "Validate Probe/Marker, returns List of ProbeMarkerDomain")
	@Path("/validateProbeMarker")
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
