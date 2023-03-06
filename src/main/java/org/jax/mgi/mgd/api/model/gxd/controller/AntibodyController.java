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
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryAntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/antibody")
@Api(value = "Antibody Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntibodyController extends BaseController<AntibodyDomain> {

	@Inject
	private AntibodyService antibodyService;

	@Override
	public SearchResults<AntibodyDomain> create(AntibodyDomain domain, User user) {
		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		results = antibodyService.create(domain, user);
		results = antibodyService.getResults(Integer.valueOf(results.items.get(0).getAntibodyKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyDomain> update(AntibodyDomain domain, User user) {
		SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
		results = antibodyService.update(domain, user);
		results = antibodyService.getResults(Integer.valueOf(results.items.get(0).getAntibodyKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyDomain> delete(Integer key, User user) {
		return antibodyService.delete(key, user);
	}
	
	@Override
	public AntibodyDomain get(Integer key) {
		return antibodyService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from gxd_antibody table")
	@Path("/getObjectCount")
	public SearchResults<AntibodyDomain> getObjectCount() {
		return antibodyService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns antibody domain")
	@Path("/search")
	public List<SlimAntibodyDomain> search(AntibodyDomain searchDomain) {
	
		List<SlimAntibodyDomain> results = new ArrayList<SlimAntibodyDomain>();

		try {
			results = antibodyService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@POST
	@ApiOperation(value = "Validate Antibody by accID, returns List of SlimAntibodyDomain")
	@Path("/validateAntibody")
	public List<SlimAntibodyDomain> validateAntibody(SlimAntibodyDomain searchDomain) {
	
		List<SlimAntibodyDomain> results = new ArrayList<SlimAntibodyDomain>();

		try {
			results = antibodyService.validateAntibody(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get list of antibody domains by marker accession id")
	@Path("/getAntibodyByMarker")
	public List<SummaryAntibodyDomain> getAntibodyByMarker(String accid) {
		
		List<SummaryAntibodyDomain> results = new ArrayList<SummaryAntibodyDomain>();

		try {
			results = antibodyService.getAntibodyByMarker(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get list of antibody domains by reference jnumid")
	@Path("/getAntibodyByRef")
	public List<SummaryAntibodyDomain> getAntibodyByRef(String jnumid) {
		
		List<SummaryAntibodyDomain> results = new ArrayList<SummaryAntibodyDomain>();

		try {
			results = antibodyService.getAntibodyByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
