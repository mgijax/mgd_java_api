package org.jax.mgi.mgd.api.model.prb.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeSourceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/source")
@Tag(name = "Source Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeSourceController extends BaseController<ProbeSourceDomain> {

	@Inject
	private ProbeSourceService probeSourceService;
	
	@Override
	public SearchResults<ProbeSourceDomain> create(ProbeSourceDomain domain, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		results = probeSourceService.create(domain, user);
		results = probeSourceService.getResults(Integer.valueOf(results.items.get(0).getSourceKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeSourceDomain> update(ProbeSourceDomain domain, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		results = probeSourceService.update(domain, user);
		results = probeSourceService.getResults(Integer.valueOf(results.items.get(0).getSourceKey()));
		return results;
	}

	@Override
	public ProbeSourceDomain get(Integer key) {
		return probeSourceService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from prb_source table")
	@Path("/getObjectCount")
	public SearchResults<ProbeSourceDomain> getObjectCount() {
		return probeSourceService.getObjectCount();
	}
	
	@Override
	public SearchResults<ProbeSourceDomain> delete(Integer key, User user) {
		return probeSourceService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search/returns probe source domain")
	@Path("/search")
	public List<ProbeSourceDomain> search(ProbeSourceDomain searchDomain) {
	
		List<ProbeSourceDomain> results = new ArrayList<ProbeSourceDomain>();

		try {
			results = probeSourceService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Operation(description = "Search Library Set")
	@Path("/searchLibrarySet")
	public List<SlimProbeSourceDomain> searchLibrarySet() {

		List<SlimProbeSourceDomain> results = new ArrayList<SlimProbeSourceDomain>();
		
		try {
			results = probeSourceService.searchLibrarySet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;	
	}
	
}
