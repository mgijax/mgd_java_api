package org.jax.mgi.mgd.api.model.acc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/accession")
@Api(value = "Accession Endpoints")
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
	@ApiOperation(value = "Search by accID only")
	@Path("/search")
	public List<AccessionDomain> search(AccessionDomain searchDomain) {
		return accessionService.search(searchDomain);
	}

	@POST
	@ApiOperation(value = "is accession id a duplicate of object/logicaldb/mgitype")
	@Path("/validIsDuplicate")
	public List<SlimAccessionDomain> validIsDuplicate(
			@ApiParam(value = "the primary key of the object")		
			@HeaderParam("key") String key, 
			@ApiParam(value = "accession id value")				
			@HeaderParam("accid") String accid, 
			@ApiParam(value = "see acc_logicaldb")					
			@HeaderParam("logicalddbKey") String logicaldbKey, 
			@ApiParam(value = "see acc_mgitype")						
			@HeaderParam("mgiTypeKey") String mgiTypeKey) {
		return accessionService.validIsDuplicate(key, accid, logicaldbKey, mgiTypeKey);
	}
		
	@POST
	@ApiOperation(value = "Get Marker Edit Accession Ids")
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
	@ApiOperation(value = "Get Marker Non-Edit Accession Ids")
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
	@ApiOperation(value = "Get Mutant Cell Line Edit Accession Ids")
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
	@ApiOperation(value = "Process Accession Ids")
	@Path("/process")
	public Boolean process(String parentKey, List<AccessionDomain> domain, String mgiTypeKey, User user) {
		return accessionService.process(parentKey, domain, mgiTypeKey, user);
		
	}

}
