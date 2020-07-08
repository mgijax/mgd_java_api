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
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyTypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/antibodytype")
@Api(value = "Antibody Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntibodyTypeController extends BaseController<AntibodyTypeDomain> {

	@Inject
	private AntibodyTypeService AntibodyTypeService;

	@Override
	public SearchResults<AntibodyTypeDomain> create(AntibodyTypeDomain domain, User user) {
		SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
		results = AntibodyTypeService.create(domain, user);
		results = AntibodyTypeService.getResults(Integer.valueOf(results.items.get(0).getAntibodyTypeKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyTypeDomain> update(AntibodyTypeDomain domain, User user) {
		SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
		results = AntibodyTypeService.update(domain, user);
		results = AntibodyTypeService.getResults(Integer.valueOf(results.items.get(0).getAntibodyTypeKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyTypeDomain> delete(Integer key, User user) {
		return AntibodyTypeService.delete(key, user);
	}
	
	@Override
	public AntibodyTypeDomain get(Integer key) {
		return AntibodyTypeService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from gxd_AntibodyType table")
	@Path("/getObjectCount")
	public SearchResults<AntibodyTypeDomain> getObjectCount() {
		return AntibodyTypeService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns antibody alias domain")
	@Path("/search")
	public List<AntibodyTypeDomain> search(AntibodyTypeDomain searchDomain) {
	
		List<AntibodyTypeDomain> results = new ArrayList<AntibodyTypeDomain>();

		try {
			results = AntibodyTypeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
