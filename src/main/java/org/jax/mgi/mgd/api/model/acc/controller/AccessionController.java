package org.jax.mgi.mgd.api.model.acc.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/accession")
@Tag(name = "Accession Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessionController extends BaseController<AccessionDomain> {

	@Inject
	private AccessionService accessionService;

	@Override
	public SearchResults<AccessionDomain> create(AccessionDomain accession, User user) {
		return accessionService.create(accession, user);
	}

	@Override
	public SearchResults<AccessionDomain> update(AccessionDomain accession, User user) {
		return accessionService.update(accession, user);
	}

	@Override
	public AccessionDomain get(Integer accessionKey) {
		return accessionService.get(accessionKey);
	}

	@Override
	public SearchResults<AccessionDomain> delete(Integer key, User user) {
		return accessionService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search by accID only")
	@Path("/search")
	public List<AccessionDomain> search(AccessionDomain searchDomain) {
		return accessionService.search(searchDomain);
	}

	@POST
	@Operation(description = "is accession id a duplicate of object/logicaldb/mgitype")
	@Path("/validIsDuplicate")
	public List<SlimAccessionDomain> validIsDuplicate(
			@Parameter(description = "the primary key of the object")		
			@HeaderParam("key") String key, 
			@Parameter(description = "accession id value")				
			@HeaderParam("accid") String accid, 
			@Parameter(description = "see acc_logicaldb")					
			@HeaderParam("logicalddbKey") String logicaldbKey, 
			@Parameter(description = "see acc_mgitype")						
			@HeaderParam("mgiTypeKey") String mgiTypeKey) {
		return accessionService.validIsDuplicate(key, accid, logicaldbKey, mgiTypeKey);
	}
	
	@POST
	@Operation(description = "Validate Strain Accession Id")
	@Path("/validStrainAccessionId")
	public List<SlimAccessionDomain> validStrainAccessionId(SlimAccessionDomain domain) {
			
		List<SlimAccessionDomain> results = new ArrayList<SlimAccessionDomain>();
		
		try {
			results = accessionService.validStrainAccessionId(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get Marker Edit Accession Ids")
	@Path("/markerEditAccessionIds")
	public List<AccessionDomain> getMarkerEditAccessionIds(Integer key) {
			
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();
		
		try {
			results = accessionService.getMarkerEditAccessionIds(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get Marker Non-Edit Accession Ids")
	@Path("/markerNonEditAccessionIds")
	public List<AccessionDomain> getMarkerNonEditAccessionIds(Integer key) {
			
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();
		
		try {
			results = accessionService.getMarkerNonEditAccessionIds(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get Mutant Cell Line Edit Accession Ids")
	@Path("/mclEditAccessionIds")
	public List<AccessionDomain> getMCLEditAccessionIds(Integer key) {
			
		List<AccessionDomain> results = new ArrayList<AccessionDomain>();
		
		try {
			results = accessionService.getMCLEditAccessionIds(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Operation(description = "Validate GO Isoform")
	@Path("/validateGOIsoform")
	public List<SlimAccessionDomain> validateMarker(SlimAccessionDomain searchDomain) {
		
		List<SlimAccessionDomain> results = new ArrayList<SlimAccessionDomain>();

		try {
			results = accessionService.validateGOIsoform(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Get list of quick searrch results by accid(s).")
	@Path("/getQSResultByAccid")
	public List<SlimAccessionDomain> getQSResultByAccid(@QueryParam("ids") String ids) {

		List<SlimAccessionDomain> results = new ArrayList<SlimAccessionDomain>();

		try {
			results = accessionService.getQSResultByAccid(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
