package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntigenService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeSourceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/antigen")
@Tag(name = "Antigen Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntigenController extends BaseController<AntigenDomain> {

	@Inject
	private AntigenService antigenService;
	@Inject
	private ProbeSourceService sourceService;
	
	@Override
	public SearchResults<AntigenDomain> create(AntigenDomain domain, User user) {
		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		
		// first create the new source
		SearchResults<ProbeSourceDomain> sourceResults = new SearchResults<ProbeSourceDomain>();
		sourceResults = sourceService.create(domain.getProbeSource(), user);
		domain.getProbeSource().setSourceKey(sourceResults.items.get(0).getSourceKey());
		
		
		// create the new antigen
		results = antigenService.create(domain, user);
		results = antigenService.getResults(Integer.valueOf(results.items.get(0).getAntigenKey()));
		return results;
	}

	@Override
	public SearchResults<AntigenDomain> update(AntigenDomain domain, User user) {
		SearchResults<AntigenDomain> results = new SearchResults<AntigenDomain>();
		SearchResults<ProbeSourceDomain> sourceResults = new SearchResults<ProbeSourceDomain>();
		
		// update the source first, note this does not work in the AntigenService
		sourceResults = sourceService.update(domain.getProbeSource(), user);
		domain.setProbeSource(sourceResults.items.get(0));
		
		// now update the antigen
		results = antigenService.update(domain, user);
		results = antigenService.getResults(Integer.valueOf(results.items.get(0).getAntigenKey()));
		
		return results;
	}

	@Override
	public SearchResults<AntigenDomain> delete(Integer key, User user) {
		return antigenService.delete(key, user);
	}
	
	@Override
	public AntigenDomain get(Integer key) {
		return antigenService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from gxd_antigen table")
	@Path("/getObjectCount")
	public SearchResults<AntigenDomain> getObjectCount() {
		return antigenService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns slim antigen domain")
	@Path("/search")
	public List<SlimAntigenDomain> search(AntigenDomain searchDomain) {
	
		List<SlimAntigenDomain> results = new ArrayList<SlimAntigenDomain>();

		try {
			results = antigenService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "validate antigen accID")
	@Path("/validateAntigenAcc")
	
	public AntigenDomain validateAntigenAcc(AntigenDomain searchDomain) {
		return antigenService.validateAntigenAcc(searchDomain);
		
	}
	
	@POST
	@Operation(description = "get antibodies by antigen key")
	@Path("/getAntibodies")
	
	public List<SlimAntibodyDomain> getAntibodies(Integer key) {
		return antigenService.getAntibodies(key);		
	}
}
