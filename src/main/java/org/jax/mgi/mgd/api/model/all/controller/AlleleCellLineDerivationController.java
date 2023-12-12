package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleCellLineDerivationService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/allelecelllinederivation")
@Tag(name = "Allele Cell Line Derivation Endpoints")
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

	@GET
	@Operation(description = "Get the object count from all_cellline_derivation table")
	@Path("/getObjectCount")
	public SearchResults<AlleleCellLineDerivationDomain> getObjectCount() {
		return derivationService.getObjectCount();
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<AlleleCellLineDerivationDomain> search(AlleleCellLineDerivationDomain searchDomain) {
			
		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();
		
		try {
			results = derivationService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
	
	@POST
	@Operation(description = "Search Mutant Cell Line Set")
	@Path("/searchMCLSet")
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
	@Operation(description = "Search Duplicate Derivation Name")
	@Path("/searchDuplicateByName")
	public List<AlleleCellLineDerivationDomain> searchDuplicateByName(AlleleCellLineDerivationDomain domain) {

		List<AlleleCellLineDerivationDomain> results = new ArrayList<AlleleCellLineDerivationDomain>();
		
		try {
			results = derivationService.searchDuplicateByName(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;	
	}
	
	@POST
	@Operation(description = "Validate Derivation")
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
