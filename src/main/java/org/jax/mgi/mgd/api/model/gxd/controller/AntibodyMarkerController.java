package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyMarkerDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyMarkerService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/antibodymarker")
@Tag(name = "Antibody Marker Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntibodyMarkerController extends BaseController<AntibodyMarkerDomain> {

	@Inject
	private AntibodyMarkerService antibodyMarkerService;

	@Override
	public SearchResults<AntibodyMarkerDomain> create(AntibodyMarkerDomain domain, User user) {
		SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
		results = antibodyMarkerService.create(domain, user);
		results = antibodyMarkerService.getResults(Integer.valueOf(results.items.get(0).getAntibodyKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyMarkerDomain> update(AntibodyMarkerDomain domain, User user) {
		SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
		results = antibodyMarkerService.update(domain, user);
		results = antibodyMarkerService.getResults(Integer.valueOf(results.items.get(0).getAntibodyKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyMarkerDomain> delete(Integer key, User user) {
		return antibodyMarkerService.delete(key, user);
	}
	
	@Override
	public AntibodyMarkerDomain get(Integer key) {
		return antibodyMarkerService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from gxd_AntibodyMarker table")
	@Path("/getObjectCount")
	public SearchResults<AntibodyMarkerDomain> getObjectCount() {
		return antibodyMarkerService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns antibody alias domain")
	@Path("/search")
	public List<AntibodyMarkerDomain> search(AntibodyMarkerDomain searchDomain) {
	
		List<AntibodyMarkerDomain> results = new ArrayList<AntibodyMarkerDomain>();

		try {
			results = antibodyMarkerService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@POST
	@Operation(description = "Validate Antibody/Marker, returns List of AntibodyMarkerDomain")
	@Path("/validateAntibodyMarker")
	public List<AntibodyMarkerDomain> validateAntibodyMarker(AntibodyMarkerDomain searchDomain) {
	
		List<AntibodyMarkerDomain> results = new ArrayList<AntibodyMarkerDomain>();

		try {
			results = antibodyMarkerService.validateAntibodyMarker(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
