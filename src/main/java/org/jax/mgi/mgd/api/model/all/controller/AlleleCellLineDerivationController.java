package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleCellLineDerivationService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelecelllinederivation")
@Api(value = "Allele Cell Line Derivation Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleCellLineDerivationController extends BaseController<AlleleCellLineDerivationDomain> {

	@Inject
	private AlleleCellLineDerivationService derivationService;

	@Override
	public SearchResults<AlleleCellLineDerivationDomain> create(AlleleCellLineDerivationDomain domain, User user) {
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		results = derivationService.create(domain, user);
		results = derivationService.getResults(Integer.valueOf(results.items.get(0).getDerivationKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleCellLineDerivationDomain> update(AlleleCellLineDerivationDomain domain, User user) {
		SearchResults<AlleleCellLineDerivationDomain> results = new SearchResults<AlleleCellLineDerivationDomain>();
		results = derivationService.update(domain, user);
		results = derivationService.getResults(Integer.valueOf(results.items.get(0).getDerivationKey()));
		return results;
	}

	@Override
	public SearchResults<AlleleCellLineDerivationDomain> delete(Integer key, User user) {
		return derivationService.delete(key, user);
	}
	
	@Override
	public AlleleCellLineDerivationDomain get(Integer key) {
		return derivationService.get(key);
	}

	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<AlleleCellLineDerivationDomain> searchMCLSet() {

		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();
		
		try {
			results = derivationService.searchMCLSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;	
	}
	
	@POST
	@ApiOperation(value = "Validate Derivation")
	@Path("/validateDerivation")
	public List<AlleleCellLineDerivationDomain> validateDerivation(SlimAlleleCellLineDerivationDomain searchDomain) {	
		
		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();

		try {
			results = derivationService.validateDerivation(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
