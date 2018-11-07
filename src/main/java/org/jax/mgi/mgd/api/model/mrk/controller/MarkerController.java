package org.jax.mgi.mgd.api.model.mrk.controller;

import java.io.IOException;
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
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIResultDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
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

	protected Logger log = Logger.getLogger(MarkerService.class);

	@Inject
	private MarkerService markerService;

	@Override
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		// the try/except method is here 
		// because the service does not seem to be picking up the exceptions
		// if results.error is null, then API assumes delete = success
		// if results.error is not null, then API assumes delete = fail
					
		try {
			results = markerService.create(domain, user);
		} catch (Exception e) {
			results.setError("Failed", e.getMessage(), Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
	}

	@Override
	public SearchResults<MarkerDomain> update(MarkerDomain marker, User user) {
		return markerService.update(marker, user);
	}

	@Override
	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	@Override
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();

		// the try/except method is here 
		// because the service does not seem to be picking up the exceptions
		// if results.error is null, then API assumes delete = success
		// if results.error is not null, then API assumes delete = fail
		
		try {
			results = markerService.delete(key, user);
		} catch (Exception e) {
			results.setError("Failed", e.getMessage(), Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "EI Search")
	@Path("/eiSearch")
	public List<MarkerEIResultDomain> eiSearch(MarkerSearchForm searchForm) {
		return markerService.eiSearch(searchForm);
	}

	@POST
	@ApiOperation(value = "EI Utilities ")
	@Path("/eiUtilities")
	public MarkerEIUtilitiesDomain eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
		return markerService.eiUtilities(searchForm);
	}
	
}
