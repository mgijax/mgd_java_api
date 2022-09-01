package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.service.SpecimenService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/specimen")
@Api(value = "Specimen Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecimenController extends BaseController<SpecimenDomain> {

	@Inject
	private SpecimenService specimenService;

	@Override
	public SearchResults<SpecimenDomain> create(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results = specimenService.create(domain, user);
		results = specimenService.getResults(Integer.valueOf(results.items.get(0).getSpecimenKey()));
		return results;
	}

	@Override
	public SearchResults<SpecimenDomain> update(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results = specimenService.update(domain, user);
		results = specimenService.getResults(Integer.valueOf(results.items.get(0).getSpecimenKey()));
		return results;
	}

	@Override
	public SearchResults<SpecimenDomain> delete(Integer key, User user) {
		return specimenService.delete(key, user);
	}
	
	@Override
	public SpecimenDomain get(Integer key) {
		return specimenService.get(key);
	}

	@POST
	@ApiOperation(value = "Get list of specimen domains by reference jnumid")
	@Path("/getSpecimenByRef")
	public List<SpecimenDomain> getSpecimenByRef(String jnumid) {
		
		List<SpecimenDomain> results = new ArrayList<SpecimenDomain>();

		try {
			results = specimenService.getSpecimenByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
