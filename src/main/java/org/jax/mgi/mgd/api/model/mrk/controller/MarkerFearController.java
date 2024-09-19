package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerFearDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerFearDomain;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerFearService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/markerfear")
@Tag(name = "Marker Fear Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerFearController extends BaseController<MarkerFearDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private MarkerFearService markerFearService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<MarkerFearDomain> create(MarkerFearDomain domain, User user) {	
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}

	@Override
	public SearchResults<MarkerFearDomain> update(MarkerFearDomain domain, User user) {
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		results = markerFearService.update(domain, user);
		return results;	
	}

	@Override
	public MarkerFearDomain get(Integer key) {
		return markerFearService.get(Integer.valueOf(key));
	}

	@Override
	public SearchResults<MarkerFearDomain> delete(Integer key, User user) {
		SearchResults<MarkerFearDomain> results = new SearchResults<MarkerFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@GET
	@Operation(description = "Get the object count from mgi_relationship_Fear_view")
	@Path("/getObjectCount")
	public SearchResults<MarkerFearDomain> getObjectCount() {
		return markerFearService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns slim marker fear domain")
	@Path("/search")
	public List<SlimMarkerFearDomain> search(MarkerFearDomain searchDomain) {
	
		List<SlimMarkerFearDomain> results = new ArrayList<SlimMarkerFearDomain>();

		try {
			results = markerFearService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
