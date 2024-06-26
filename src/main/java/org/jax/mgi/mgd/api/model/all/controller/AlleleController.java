package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.domain.SummaryAlleleDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleService;
import org.jax.mgi.mgd.api.model.all.service.CellLineService;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/allele")
@Tag(name = "Allele Endpoints")
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
	@Operation(description = "Get the object count from all_allele table")
	@Path("/getObjectCount")
	public SearchResults<AlleleDomain> getObjectCount() {
		return alleleService.getObjectCount();
	}
	
	@POST
	@Operation(description = "Search")
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
	@Operation(description = "Search by Variants")
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
	@Operation(description = "Validate allele symbol (status Approved, AutoLoad) OR accID, returns List of SlimAlleleDomains")
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
	@Operation(description = "Validate allele symbol (all statuses) OR accID, returns List of SlimAlleleDomains")
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
	@Operation(description = "Validate allele & conditional rules, returns List of AllelePairDomain")
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
	@Operation(description = "Get SlimAllele by Mutant Cell Line key")
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
	@Operation(description = "Get Allele/Image Panes Assoc by Image key")
	@Path("/getAlleleByImagePane")
	public List<SlimAlleleDomain> getAlleleByImagePane(SlimImageDomain searchDomain) {

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		try {
			results = alleleService.getAlleleByImagePane(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Get list of allele domains by marker accession id")
	@Path("/getAlleleByMarker")
	public SearchResults<SummaryAlleleDomain> getAlleleByMarker(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummaryAlleleDomain> results = new SearchResults<SummaryAlleleDomain>();

		try {
			results = alleleService.getAlleleByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadAlleleByMarker")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadAlleleByMarker(@QueryParam("accid") String accid) {
		return alleleService.downloadAlleleByMarker(accid);
	}
	
	@GET
	@Operation(description = "Get list of allele domains by reference jnum id")
	@Path("/getAlleleByRef")
	public SearchResults<SummaryAlleleDomain> getAlleleByRef(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummaryAlleleDomain> results = new SearchResults<SummaryAlleleDomain>();

		try {
			results = alleleService.getAlleleByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadAlleleByRef")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadAlleleByRef(@QueryParam("accid") String accid) {
		return alleleService.downloadAlleleByRef(accid);
	}	
}
