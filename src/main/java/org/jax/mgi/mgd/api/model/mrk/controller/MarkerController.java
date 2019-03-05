package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/marker")
@Api(value = "Marker Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerController extends BaseController<MarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private MarkerService markerService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {	
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results = markerService.create(domain, user);
		results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
		return results;
	}

	@Override
	public SearchResults<MarkerDomain> update(MarkerDomain domain, User user) {
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results = markerService.update(domain, user);
		results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
		return results;	
	}

	@Override
	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	@Override
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		return markerService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "EI Search/returns slim marker domain")
	@Path("/eiSearch")
	public List<SlimMarkerDomain> eiSearch(MarkerDomain searchDomain) {
			
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();
		
		try {
			results = markerService.eiSearch(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get Alias by marker key/returns slim marker domain")
	@Path("/alias")
	public List<SlimMarkerDomain> getAlias(Integer key) {
		return markerService.getAlias(key);
	}

	@GET
	@ApiOperation(value = "Validate marker symbol/any status is valid/returns slim marker domain")
	@Path("/validAnyStatus/{symbol}")
	public List<SlimMarkerDomain> validateAnyStatus(
			@PathParam("symbol") 
			@ApiParam(value = "Validating Marker Symbol") 
			String symbol) {
		return markerService.valid(symbol, true, true);
	}
	
	@GET
	@ApiOperation(value = "Validate marker symbol/official status is valid/returns slim marker domain")
	@Path("/validOfficialStatus/{symbol}")
	public List<SlimMarkerDomain> validOfficialStatus(
			@PathParam("symbol") 
			@ApiParam(value = "Validating Marker Symbol") 
			String symbol) {
		return markerService.valid(symbol, false, false);
	}
			
	@POST
	@ApiOperation(value = "EI Utilities ")
	@Path("/eiUtilities")
	public SearchResults<MarkerDomain> eiUtilities(MarkerUtilitiesForm searchForm) {

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		
		try {
			log.info(Constants.LOG_IN_JSON);
			log.info(mapper.writeValueAsString(searchForm));				
			results = markerService.eiUtilities(searchForm);
		} catch (Exception e) {
			results.setError(Constants.LOG_FAIL_DOMAIN, getRootException(e).getMessage(), Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
	}
	
}
