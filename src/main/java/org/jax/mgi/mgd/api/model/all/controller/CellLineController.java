package org.jax.mgi.mgd.api.model.all.controller;

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
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimCellLineDomain;
import org.jax.mgi.mgd.api.model.all.service.CellLineService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/cellline")
@Api(value = "Cell Line Endpoints")
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
	@ApiOperation(value = "Get the mutant cell line count from all_cellline table")
	@Path("/getMutantCellLineCount")
	public SearchResults<CellLineDomain> getMutantCellLineCount() {
		return cellLineService.getMutantCellLineCount();
	}

	@GET
	@ApiOperation(value = "Get the parent cell line count from all_cellline table")
	@Path("/getParentCellLineCount")
	public SearchResults<CellLineDomain> getParentCellLineCount() {
		return cellLineService.getParentCellLineCount();
	}

	@POST
	@ApiOperation(value = "Get the mutant cell line count by parent cell line")
	@Path("/getMCLCountByParentCellLine")
	public SearchResults<CellLineDomain> getMCLCountByParentCellLine(Integer key) {
		return cellLineService.getMCLCountByParentCellLine(key);
	}
	
	@POST
	@ApiOperation(value = "Get the mutant cell line count by derivation")
	@Path("/getMCLCountByDerivation")
	public SearchResults<CellLineDomain> getMCLCountByDerivation(Integer key) {
		return cellLineService.getMCLCountByDerivation(key);
	}
	
	@POST
	@ApiOperation(value = "Search Mutant Cell Lines")
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
	@ApiOperation(value = "Search Parent Cell Lines")
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
	@ApiOperation(value = "Validate Mutant Cell Line")
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
	@ApiOperation(value = "Validate Parent Cell Line")
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
