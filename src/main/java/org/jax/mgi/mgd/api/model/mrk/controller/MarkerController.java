package org.jax.mgi.mgd.api.model.mrk.controller;

import java.io.IOException;
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
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesDomain;
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

	// the try/except methods are in the Controller 
	// because the Service does not seem to be picking up the exceptions
	// if results.error is null, then API assumes success
	// if results.error is not null, then API assumes fail
	
	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();

	@Inject
	private MarkerService markerService;

	@Override
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		try {
			log.info(Constants.LOG_CREATE_BEFORE_JSON);
			log.info(mapper.writeValueAsString(domain));		
		} catch (Exception e) {	
			results.setError(Constants.LOG_FAIL_JSON, e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
				
		try {
			results = markerService.create(domain, user);
			results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
			log.info(Constants.LOG_CREATE_AFTER_RESULTS);
			log.info(mapper.writeValueAsString(results.items.get(0)));
		} catch (Exception e) {
			results.setError(Constants.LOG_FAIL_ENTITY, e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;
	}

	@Override
	public SearchResults<MarkerDomain> update(MarkerDomain domain, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		try {
			log.info(Constants.LOG_UPDATE_BEFORE_JSON);
			log.info(mapper.writeValueAsString(domain));		
		} catch (Exception e) {	
			results.setError(Constants.LOG_FAIL_JSON, e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		try {
			results = markerService.update(domain, user);
			results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
			log.info(Constants.LOG_UPDATE_AFTER_RESULTS);
			log.info(mapper.writeValueAsString(results.items.get(0)));		
		} catch (Exception e) {	
			results.setError(Constants.LOG_FAIL_ENTITY, e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}

		return results;	
	}

	@Override
	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	@Override
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		try {
			log.info(Constants.LOG_DELETE_BEFORE_PKEY);
			log.info(mapper.writeValueAsString(key));		
		} catch (Exception e) {	
			results.setError(Constants.LOG_FAIL_PKEY, e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
				
		try {
			results = markerService.delete(key, user);
		} catch (Exception e) {
			results.setError(Constants.LOG_FAIL_JSON, e.getMessage(), Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
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
	public MarkerEIUtilitiesDomain eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
		return markerService.eiUtilities(searchForm);
	}
	
}
