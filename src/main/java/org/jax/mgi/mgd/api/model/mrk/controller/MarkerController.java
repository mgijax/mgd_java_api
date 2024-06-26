package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerOfficialChromDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SummaryMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/marker")
@Tag(name = "Marker Endpoints")
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
		
		// if non-mouse, then return
		if (!results.items.get(0).getOrganismKey().equals("1")) {
			results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
			return results;
		}
		
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

		// if non-mouse, then return
		if (!results.items.get(0).getOrganismKey().equals("1")) {
			results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
			return results;
		}
		
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
		
		MarkerDomain results = new MarkerDomain();
		
		results = markerService.get(markerKey);
		
		// attach summary links
		try {
			results = markerService.getSummaryLinks(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Override
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		return markerService.delete(key, user);
	}

	@GET
	@Operation(description = "Get the object count from mrk_marker table")
	@Path("/getObjectCount")
	public SearchResults<MarkerDomain> getObjectCount() {
		return markerService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns slim marker domain")
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
	@Operation(description = "Get next Gm symbol that is available in the sequence")
	@Path("/getNextGmSequence")
	public List<SlimMarkerDomain> getNextGmSequence() {
		return markerService.getNextGmSequence();
	}
	
	@POST
	@Operation(description = "Get next Rr symbol that is available in the sequence")
	@Path("/getNextRrSequence")
	public List<SlimMarkerDomain> getNextRrSequence() {
		return markerService.getNextRrSequence();
	}
	
	@GET
	@Operation(description = "Get list of marker domains by reference jnum id")
	@Path("/getMarkerByRef")
	public SearchResults<SummaryMarkerDomain> getMarkerByRef(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummaryMarkerDomain> results = new SearchResults<SummaryMarkerDomain>();

		try {
			results = markerService.getMarkerByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadMarkerByRef")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadMarkerByRef(@QueryParam("accid") String accid) {
		return markerService.downloadMarkerByRef(accid);
	}
	
	@GET
	@Operation(description = "Validate marker symbol/any status is valid/returns slim marker domain")
	@Path("/validateAnyStatus/{symbol}")
	public List<SlimMarkerDomain> validateAnyStatus(
			@PathParam("symbol") 
			@Parameter(description = "Validating Marker Symbol") 
			String symbol) {
		return markerService.validate(symbol, true, true);
	}
	
	@GET
	@Operation(description = "Validate marker symbol/official status is valid/returns slim marker domain")
	@Path("/validateOfficialStatus/{symbol}")
	public List<SlimMarkerDomain> validateOfficialStatus(
			@PathParam("symbol") 
			@Parameter(description = "Validating Marker Symbol") 
			String symbol) {
		return markerService.validate(symbol, false, false);
	}
	
	@POST
	@Operation(description = "Validate Marker by Marker Symbol(=), Chromosome or AccID (=)")
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
	@Operation(description = "Validate marker symbol/official status/chromosome match/returns slim markeroffsetchrom domain")
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
	@Operation(description = "Validate marker feature types/check result error")
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
	@Operation(description = "EI Utilities ")
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
