package org.jax.mgi.mgd.api.model.gxd.controller;

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
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyMarkerDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyMarkerService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/antibodymarker")
@Api(value = "Antibody Marker Endpoints")
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
	@ApiOperation(value = "Get the object count from gxd_AntibodyMarker table")
	@Path("/getObjectCount")
	public SearchResults<AntibodyMarkerDomain> getObjectCount() {
		return antibodyMarkerService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns antibody alias domain")
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
	@ApiOperation(value = "Validate Antibody/Marker, returns List of AntibodyMarkerDomain")
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
