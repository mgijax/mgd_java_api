package org.jax.mgi.mgd.api.model.mrk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	
	protected Logger log = Logger.getLogger(MarkerService.class);

	@Inject
	private MarkerService markerService;

	@Override
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		try {
			results = markerService.create(domain, user);
			results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
		} catch (Exception e) {
			results.setError("Failed : create", e.getMessage(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		return results;
	}

	@Override
	public SearchResults<MarkerDomain> update(MarkerDomain domain, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		try {
			results = markerService.update(domain, user);
			results = markerService.getResults(Integer.valueOf(results.items.get(0).getMarkerKey()));
		} catch (Exception e) {
			results.setError("Failed : update", e.getMessage(), Constants.HTTP_SERVER_ERROR);
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
			results = markerService.delete(key, user);
		} catch (Exception e) {
			results.setError("Failed : delete", e.getMessage(), Constants.HTTP_SERVER_ERROR);
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
	@ApiOperation(value = "Alias search by marker key/returns slim marker domain")
	@Path("/aliasSearch")
	public List<SlimMarkerDomain> aliasSearch(Integer key) {
		return markerService.aliasSearch(key);
	}

	@POST
	@ApiOperation(value = "Validate marker symbol/any status is valid/returns slim marker domain")
	@Path("/validAnyStatus")
	public List<SlimMarkerDomain> validateAnyStatus(String value) {
		return markerService.valid(value, true, true);
	}
	
	@POST
	@ApiOperation(value = "Validate marker symbol/official status is valid/returns slim marker domain")
	@Path("/validOfficial")
	public List<SlimMarkerDomain> validMarker(String value) {
		return markerService.valid(value, false, false);
	}
			
	@POST
	@ApiOperation(value = "EI Utilities ")
	@Path("/eiUtilities")
	public MarkerEIUtilitiesDomain eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
		return markerService.eiUtilities(searchForm);
	}
	
}
