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
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleService;
import org.jax.mgi.mgd.api.model.all.service.CellLineService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allele")
@Api(value = "Allele Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleController extends BaseController<AlleleDomain> {

	@Inject
	private AlleleService alleleService;
	@Inject
	private CellLineService cellLineService;
	
	@Override
	public SearchResults<AlleleDomain> create(AlleleDomain domain, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		SearchResults<CellLineDomain> cellLineResults = new SearchResults<CellLineDomain>();
		
		if (domain.getMutantCellLineAssocs() != null && !domain.getMutantCellLineAssocs().get(0).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			cellLineResults = cellLineService.createMutantCellLine(domain.getAlleleTypeKey(), domain.getMutantCellLineAssocs().get(0).getMutantCellLine(), user);
			if (cellLineResults.items != null) {
	    		domain.getMutantCellLineAssocs().get(0).getMutantCellLine().setCellLineKey(cellLineResults.items.get(0).getCellLineKey());        		
			}
		}

		if (!cellLineResults.error.isEmpty()) {		
			results = alleleService.create(domain, user);
		}
		results = alleleService.getResults(Integer.valueOf(results.items.get(0).getAlleleKey()));
		
		if (!cellLineResults.error.isEmpty()) {
			results.setError("Add", cellLineResults.error, Constants.HTTP_SERVER_ERROR);
		}
				
		return results;	
	}

	@Override
	public SearchResults<AlleleDomain> update(AlleleDomain domain, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		SearchResults<CellLineDomain> cellLineResults = new SearchResults<CellLineDomain>();
		
		if (domain.getMutantCellLineAssocs() != null && !domain.getMutantCellLineAssocs().get(0).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			cellLineResults = cellLineService.createMutantCellLine(domain.getAlleleTypeKey(), domain.getMutantCellLineAssocs().get(0).getMutantCellLine(), user);
			if (cellLineResults.items != null) {
	    		domain.getMutantCellLineAssocs().get(0).getMutantCellLine().setCellLineKey(cellLineResults.items.get(0).getCellLineKey());        		
			}
		}
		
		if (!cellLineResults.error.isEmpty()) {		
			results = alleleService.update(domain, user);				
		}
		
		results = alleleService.getResults(Integer.valueOf(results.items.get(0).getAlleleKey()));
		
		if (!cellLineResults.error.isEmpty()) {
			results.setError("Modify", cellLineResults.error, Constants.HTTP_SERVER_ERROR);
		}
		
		return results;		
	}

	@Override
	public AlleleDomain get(Integer alleleKey) {
		return alleleService.get(alleleKey);
	}

	@Override
	public SearchResults<AlleleDomain> delete(Integer key, User user) {
		return alleleService.delete(key, user);
	}

	@GET
	@ApiOperation(value = "Get the object count from all_allele table")
	@Path("/getObjectCount")
	public SearchResults<AlleleDomain> getObjectCount() {
		return alleleService.getObjectCount();
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<SlimAlleleDomain> search(AlleleDomain searchDomain) {
			
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		try {
			results = alleleService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Search by Variants")
	@Path("/searchVariant")
	public List<SlimAlleleRefAssocDomain> searchVariant(AlleleDomain searchDomain) {
			
		List<SlimAlleleRefAssocDomain> results = new ArrayList<SlimAlleleRefAssocDomain>();
		
		try {
			results = alleleService.searchVariant(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Validate allele symbol (status Approved, AutoLoad) OR accID, returns List of SlimAlleleDomains")
	@Path("/validateAllele")
	public List<SlimAlleleDomain> validateAllele(SlimAlleleDomain searchDomain) {
	
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();

		try {
			results = alleleService.validateAllele(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Validate allele symbol (all statuses) OR accID, returns List of SlimAlleleDomains")
	@Path("/validateAlleleAnyStatus")
	public List<SlimAlleleRefAssocDomain> validateAlleleAnyStatus(SlimAlleleRefAssocDomain searchDomain) {
	
		List<SlimAlleleRefAssocDomain> results = new ArrayList<SlimAlleleRefAssocDomain>();

		try {
			results = alleleService.validateAlleleAnyStatus(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
}
