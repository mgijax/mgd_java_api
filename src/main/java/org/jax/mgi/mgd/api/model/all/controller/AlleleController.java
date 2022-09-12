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
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
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

		log.info("alleleController/create");
		
		if (domain.getMutantCellLineAssocs() != null) {
			cellLineResults = cellLineService.createMutantCellLine(domain.getAlleleTypeKey(), domain.getMutantCellLineAssocs().get(0).getMutantCellLine(), user);
	    	log.info("alleleController/create/checking cellLineResults.error: 1");		
			if (cellLineResults.items != null) {					
				log.info("alleleController/create/cellLineResults.items created");
				domain.getMutantCellLineAssocs().get(0).getMutantCellLine().setCellLineKey(cellLineResults.items.get(0).getCellLineKey());        		
			}
		}
		
		if (cellLineResults.error == null || cellLineResults.error.isEmpty()) {		
	    	log.info("alleleController/will call alleleService/create()");
			results = alleleService.create(domain, user);
			results = alleleService.getResults(Integer.valueOf(results.items.get(0).getAlleleKey()));
		}
		
		if (cellLineResults.error != null && !cellLineResults.error.isEmpty()) {
			results.setError("Add", cellLineResults.error, Constants.HTTP_SERVER_ERROR);
		}
				
		return results;	
	}

	@Override
	public SearchResults<AlleleDomain> update(AlleleDomain domain, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		SearchResults<CellLineDomain> cellLineResults = new SearchResults<CellLineDomain>();
		cellLineResults.error = "";
		
		log.info("alleleController/update");
		
		if (domain.getMutantCellLineAssocs() != null) {
			cellLineResults = cellLineService.createMutantCellLine(domain.getAlleleTypeKey(), domain.getMutantCellLineAssocs().get(0).getMutantCellLine(), user);
			log.info("alleleController/update/cellLineResults.items created");
			if (cellLineResults.items != null) {
				domain.getMutantCellLineAssocs().get(0).getMutantCellLine().setCellLineKey(cellLineResults.items.get(0).getCellLineKey());        		
			}
		}
		
		if (cellLineResults.error == null || cellLineResults.error.isEmpty()) {		
			results = alleleService.update(domain, user);				
		}
			
		results = alleleService.getResults(Integer.valueOf(domain.getAlleleKey()));		
		
		if (cellLineResults.error != null && !cellLineResults.error.isEmpty()) {
			results.setError("Modify", cellLineResults.error, Constants.HTTP_SERVER_ERROR);
		}
		
		return results;		
	}

	@Override
	public AlleleDomain get(Integer key) {
		return alleleService.get(key);
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
	
	@POST
	@ApiOperation(value = "Validate allele & conditional rules, returns List of AllelePairDomain")
	@Path("/validateAlleleConditional")
	public List<SlimAlleleDomain> validateAlleleConditional(List<AllelePairDomain> searchDomain) {
	
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();		

		try {
			results = alleleService.validateAlleleConditional(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get SlimAllele by Mutant Cell Line key")
	@Path("/getSlimByMCL")
	public List<SlimAlleleDomain> getSlimByMCL(Integer key) {
			
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		try {
			results = alleleService.getSlimByMCL(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get Allele/Image Panes Assoc by Image key")
	@Path("/getAlleleByImagePane")
	public List<SlimAlleleDomain> getAlleleByImage(SlimImageDomain searchDomain) {
	
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();

		try {
			results = alleleService.getAlleleByImagePane(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get list of allele domains by marker accession id")
	@Path("/getAlleleByMarker")
	public List<AlleleDomain> getAlleleByMarker(String accid) {
		
		List<AlleleDomain> results = new ArrayList<AlleleDomain>();

		try {
			results = alleleService.getAlleleByMarker(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get list of allele domains by reference jnumid")
	@Path("/getAlleleByRef")
	public List<AlleleDomain> getAlleleByRef(String jnumid) {
		
		List<AlleleDomain> results = new ArrayList<AlleleDomain>();

		try {
			results = alleleService.getAlleleByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
