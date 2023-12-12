package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimCellLineDomain;
import org.jax.mgi.mgd.api.model.all.service.CellLineService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/cellline")
@Tag(name = "Cell Line Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CellLineController extends BaseController<CellLineDomain> {

	@Inject
	private CellLineService cellLineService;

	@Override
	public SearchResults<CellLineDomain> create(CellLineDomain domain, User user) {
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		results = cellLineService.create(domain, user);
		results = cellLineService.getResults(Integer.valueOf(results.items.get(0).getCellLineKey()));
		return results;	
	}

	@Override
	public SearchResults<CellLineDomain> update(CellLineDomain domain, User user) {
		SearchResults<CellLineDomain> results = new SearchResults<CellLineDomain>();
		results = cellLineService.update(domain, user);				
		results = cellLineService.getResults(Integer.valueOf(results.items.get(0).getCellLineKey()));
		return results;		
	}

	@Override
	public SearchResults<CellLineDomain> delete(Integer key, User user) {
		return cellLineService.delete(key, user);
	}
	
	@Override
	public CellLineDomain get(Integer key) {
		return cellLineService.get(key);
	}

	@GET
	@Operation(description = "Get the mutant cell line count from all_cellline table")
	@Path("/getMutantCellLineCount")
	public SearchResults<CellLineDomain> getMutantCellLineCount() {
		return cellLineService.getMutantCellLineCount();
	}

	@GET
	@Operation(description = "Get the parent cell line count from all_cellline table")
	@Path("/getParentCellLineCount")
	public SearchResults<CellLineDomain> getParentCellLineCount() {
		return cellLineService.getParentCellLineCount();
	}

	@POST
	@Operation(description = "Get the mutant cell line count by parent cell line")
	@Path("/getMCLCountByParentCellLine")
	public SearchResults<CellLineDomain> getMCLCountByParentCellLine(Integer key) {
		return cellLineService.getMCLCountByParentCellLine(key);
	}
	
	@POST
	@Operation(description = "Get the mutant cell line count by derivation")
	@Path("/getMCLCountByDerivation")
	public SearchResults<CellLineDomain> getMCLCountByDerivation(Integer key) {
		return cellLineService.getMCLCountByDerivation(key);
	}
	
	@POST
	@Operation(description = "Search Mutant Cell Lines")
	@Path("/searchMutantCellLines")
	public List<SlimCellLineDomain> searchMutantCellLines(CellLineDomain searchDomain) {

		List<SlimCellLineDomain> results = new ArrayList<SlimCellLineDomain>();
		
		try {
			results = cellLineService.searchMutantCellLines(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;	
	}
	
	@POST
	@Operation(description = "Search Parent Cell Lines")
	@Path("/searchParentCellLines")
	public List<CellLineDomain> searchParentCellLines(CellLineDomain searchDomain) {

		List<CellLineDomain> results = new ArrayList<CellLineDomain>();
		
		try {
			results = cellLineService.searchParentCellLines(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;	
	}
	
	@POST
	@Operation(description = "Validate Mutant Cell Line")
	@Path("/validateMutantCellLine")
	public List<CellLineDomain> validateMutantCellLine(CellLineDomain searchDomain) {
	
		List<CellLineDomain> results = new ArrayList<CellLineDomain>();

		try {
			results = cellLineService.validateMutantCellLine(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Validate Parent Cell Line")
	@Path("/validateParentCellLine")
	public List<CellLineDomain> validateParentCellLine(CellLineDomain searchDomain) {
	
		List<CellLineDomain> results = new ArrayList<CellLineDomain>();

		try {
			results = cellLineService.validateParentCellLine(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
