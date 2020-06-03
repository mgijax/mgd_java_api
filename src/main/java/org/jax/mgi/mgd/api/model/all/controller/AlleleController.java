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
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleCellLineDerivationService;
import org.jax.mgi.mgd.api.model.all.service.AlleleService;
import org.jax.mgi.mgd.api.model.all.service.CellLineService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allele")
@Api(value = "Allele Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleController extends BaseController<AlleleDomain> {

	@Inject
	private AlleleService alleleService;
	@Inject
	private AlleleCellLineDerivationService derivationService;
	@Inject
	private CellLineService cellLineService;
	
	@Override
	public SearchResults<AlleleDomain> create(AlleleDomain domain, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		results = alleleService.create(domain, user);
		results = alleleService.getResults(Integer.valueOf(results.items.get(0).getAlleleKey()));
		return results;	
	}

	@Override
	public SearchResults<AlleleDomain> update(AlleleDomain domain, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();

        //
		// for 1st row only...
        // check isParent, isMutant to see if a new cell line needs to be created
        //

		SlimAlleleCellLineDerivationDomain derivationSearch = new SlimAlleleCellLineDerivationDomain();
		List<AlleleCellLineDerivationDomain> derivationResults = new ArrayList<AlleleCellLineDerivationDomain>();
    	CellLineDomain cellLineDomain = new CellLineDomain();

        // set the isParent
        // default cellLineType = Embryonic Stem Cell (3982968)		
		Boolean isParent = true;
		Boolean isMutant = true;

		String cellLineTypeKey = domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineTypeKey();
		if (cellLineTypeKey.isEmpty()) {
          isParent = false;
          cellLineTypeKey = "3982968";          
        };

        if (domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineKey().isEmpty()) {
        	isMutant = false;
		}
        
        if (isParent == false && isMutant == false) {

        	log.info("processAlleleCellLine/isParent == false && isMutant == false");

        	// not specified
        	//if = "Gene trapped" or "Targeted"
        	if (domain.getAlleleTypeKey().equals("847121") || domain.getAlleleTypeKey().equals("847116")) {

            	log.info("processAlleleCellLine: " + domain.getAlleleTypeKey());

	            // select the derivation key that is associated with:
	            //   allele type
	            //   creator = Not Specified (3982966)
	            //   vector = Not Specified (4311225)           		
	            //   vector type = Not Specified (3982979)
	            //   parent cell line = Not Specified (-1)
	            //   strain = Not Specified (-1)
	            //   cell line type
	            //
            	
        		derivationSearch.setAlleleTypeKey(domain.getAlleleTypeKey());
        		derivationSearch.setVectorKey("4311225");
        		derivationSearch.setVectorTypeKey("3982979");
        		derivationSearch.setParentCellLineKey("-1");
        		derivationSearch.setCreatorKey("3982966");
        		derivationSearch.setStrainKey("-1");
        		derivationSearch.setCellLineTypeKey(cellLineTypeKey);
        		
        		derivationResults = derivationService.validateDerivation(derivationSearch);
        		
        		if (!derivationResults.get(0).getDerivationKey().isEmpty()) {       		
	        		log.info("processAlleleCellLine/validated derivation: " + derivationResults.get(0).getDerivationKey());       		
	        		log.info("processAlleleCellLine/create new cell line");
	        		cellLineDomain.setCellLine("Not Specified");
	        		cellLineDomain.setStrainKey("-1");
	        		cellLineDomain.setCellLineTypeKey(cellLineTypeKey);
	        		cellLineDomain.setDerivation(derivationResults.get(0));				
	        		cellLineDomain.setIsMutant("1");
	        		SearchResults<CellLineDomain> cellLineResults = new SearchResults<CellLineDomain>();
	        		log.info("processAlleleCellLine/calling cellLineService.create()");				
	        		cellLineResults = cellLineService.create(cellLineDomain, user);
	        		domain.getMutantCellLineAssocs().get(0).getMutantCellLine().setCellLineKey(cellLineResults.items.get(0).getCellLineKey());        		
        		}
        	}         	
        }
        
        //
        // end check isParent, isMutant to see if a new cell line needs to be created
        //
		
        log.info("processAlleleController/cell line (0):" + domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineKey());
		results = alleleService.update(domain, user);				
		results = alleleService.getResults(Integer.valueOf(results.items.get(0).getAlleleKey()));
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
