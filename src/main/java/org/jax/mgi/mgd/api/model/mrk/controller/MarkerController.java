package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerOfficialChromDomain;
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
		
		// to update the mrk_location_cache table						
		try {
			log.info("processMarker/mrkLocationUtilities");
			markerService.mrklocationUtilities(results.items.get(0).getMarkerKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// to update the mrk_mcv_cache table				
		try {
			log.info("processMarker/mrkmcvUtilities");
			markerService.mrkmcvUtilities(results.items.get(0).getMarkerKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// to update the mrk_reference_cache table		
		try {
			log.info("processMarker/mrkrefByMarkerUtilities");
			markerService.mrkrefByMarkerUtilities(results.items.get(0).getMarkerKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
		return results;
	}

	@Override
	public SearchResults<MarkerDomain> update(MarkerDomain domain, User user) {

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results = markerService.update(domain, user);

		// to update the mrk_location_cache table						
		try {
			log.info("processMarker/mrkLocationUtilities");
			markerService.mrklocationUtilities(results.items.get(0).getMarkerKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// to update the mrk_mcv_cache table				
		try {
			log.info("processMarker/mrkmcvUtilities");
			markerService.mrkmcvUtilities(results.items.get(0).getMarkerKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// to update the mrk_reference_cache table		
		try {
			log.info("processMarker/mrkrefByMarkerUtilities/start");
			markerService.mrkrefByMarkerUtilities(results.items.get(0).getMarkerKey());
			log.info("processMarker/mrkrefBymarkerUtilities/end");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
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

	@GET
	@ApiOperation(value = "Get the object count from mrk_marker table")
	@Path("/getObjectCount")
	public SearchResults<MarkerDomain> getObjectCount() {
		return markerService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim marker domain")
	@Path("/search")
	public List<SlimMarkerDomain> search(MarkerDomain searchDomain) {
	
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		try {
			results = markerService.search(searchDomain);
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

	@POST
	@ApiOperation(value = "Get next Gm symbol that is available in the sequence")
	@Path("/getNextGmSequence")
	public List<SlimMarkerDomain> getNextGmSequence() {
		return markerService.getNextGmSequence();
	}
	
	@POST
	@ApiOperation(value = "Get list of marker domains by reference jnumid")
	@Path("/getMarkerByRef")
	public List<SlimMarkerDomain> getMarkerByRef(String jnumid) {
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		try {
			results = markerService.getMarkerByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Validate marker symbol/any status is valid/returns slim marker domain")
	@Path("/validateAnyStatus/{symbol}")
	public List<SlimMarkerDomain> validateAnyStatus(
			@PathParam("symbol") 
			@ApiParam(value = "Validating Marker Symbol") 
			String symbol) {
		return markerService.validate(symbol, true, true);
	}
	
	@GET
	@ApiOperation(value = "Validate marker symbol/official status is valid/returns slim marker domain")
	@Path("/validateOfficialStatus/{symbol}")
	public List<SlimMarkerDomain> validateOfficialStatus(
			@PathParam("symbol") 
			@ApiParam(value = "Validating Marker Symbol") 
			String symbol) {
		return markerService.validate(symbol, false, false);
	}
	
	@POST
	@ApiOperation(value = "Validate Marker by Marker Symbol(=), Chromosome or AccID (=)")
	@Path("/validateMarker")
	public List<SlimMarkerDomain> validateMarker(SlimMarkerDomain searchDomain) {
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		try {
			results = markerService.validateMarker(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Validate marker symbol/official status/chromosome match/returns slim markeroffsetchrom domain")
	@Path("/validateOfficialChrom")
	public SearchResults<SlimMarkerOfficialChromDomain> validateOfficialChrom(SlimMarkerOfficialChromDomain searchDomain) {
	
		SearchResults<SlimMarkerOfficialChromDomain> results = new SearchResults<SlimMarkerOfficialChromDomain>();

		try {
			results = markerService.validateOfficialChrom(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Validate marker feature types/check result error")
	@Path("/validateFeatureTypes")
	public SearchResults<SlimMarkerFeatureTypeDomain> validateFeatureTypes(SlimMarkerFeatureTypeDomain searchDomain) {
	
		SearchResults<SlimMarkerFeatureTypeDomain> results = new SearchResults<SlimMarkerFeatureTypeDomain>();

		try {
			results = markerService.validateFeatureTypes(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "EI Utilities ")
	@Path("/eiUtilities")
	public SearchResults<SlimMarkerDomain> eiUtilities(
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			MarkerUtilitiesForm searchForm) {

		SearchResults<SlimMarkerDomain> results = new SearchResults<SlimMarkerDomain>();
		
		try {
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				log.info(Constants.LOG_IN_JSON);
				log.info(mapper.writeValueAsString(searchForm));				
				results = markerService.eiUtilities(searchForm, user);
				log.info(Constants.LOG_OUT_DOMAIN);					
			} else {				
				results.setError(Constants.LOG_FAIL_USERAUTHENTICATION, api_access_token + "," + username, Constants.HTTP_SERVER_ERROR);				
			}
		} catch (Exception e) {
			results.setError(Constants.LOG_FAIL_EIUTILITIES, getRootException(e).getMessage(), Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
	}
	
}
