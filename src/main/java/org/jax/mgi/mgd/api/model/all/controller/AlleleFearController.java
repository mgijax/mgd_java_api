package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFearRegionDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleFearService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
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

@Path("/allelefear")
@Schema(description = "Allele Fear Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleFearController extends BaseController<AlleleFearDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private AlleleFearService alleleFearService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<AlleleFearDomain> create(AlleleFearDomain domain, User user) {	
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}

	@Override
	public SearchResults<AlleleFearDomain> update(AlleleFearDomain domain, User user) {
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results = alleleFearService.update(domain, user);
		return results;	
	}

	@Override
	public AlleleFearDomain get(Integer key) {
		return alleleFearService.get(Integer.valueOf(key));
	}

	@Override
	public SearchResults<AlleleFearDomain> delete(Integer key, User user) {
		SearchResults<AlleleFearDomain> results = new SearchResults<AlleleFearDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@GET
	@Operation(description = "Get the object count from mgi_relationship_Fear_view")
	@Path("/getObjectCount")
	public SearchResults<AlleleFearDomain> getObjectCount() {
		return alleleFearService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns slim allele fear domain")
	@Path("/search")
	public List<SlimAlleleFearDomain> search(AlleleFearDomain searchDomain) {
	
		List<SlimAlleleFearDomain> results = new ArrayList<SlimAlleleFearDomain>();

		try {
			results = alleleFearService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get Marker Region by Chr, Start Coord, End Coord")
	@Path("/getMarkerByRegion")
	public List<SlimMarkerDomain> getMarkerByRegion(SlimAlleleFearRegionDomain searchDomain) {
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		try {
			results = alleleFearService.getMarkerByRegion(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
