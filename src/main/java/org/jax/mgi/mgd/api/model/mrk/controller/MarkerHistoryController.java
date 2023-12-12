package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerHistoryService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/markerHistory")
@Tag(name = "Marker History Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerHistoryController extends BaseController<MarkerHistoryDomain> {

	@Inject
	private MarkerHistoryService markerHistoryService;

	@Override
	public SearchResults<MarkerHistoryDomain> create(MarkerHistoryDomain markerHistory, User user) {
		return markerHistoryService.create(markerHistory, user);
	}

	@Override
	public SearchResults<MarkerHistoryDomain> update(MarkerHistoryDomain markerHistory, User user) {
		return markerHistoryService.update(markerHistory, user);
	}

	@Override
	public MarkerHistoryDomain get(Integer key) {
		return markerHistoryService.get(key);
	}

	public SearchResults<MarkerHistoryDomain> delete(Integer key, User user) {
		// this table contains a compound primary key
		// deletes to this table are implemented in the parent's "update" method
		return null;
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<MarkerHistoryDomain> search(Integer key) {
		return markerHistoryService.search(key);
	}
	
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<MarkerHistoryDomain> domain, User user) {
		return markerHistoryService.process(parentKey, domain, user);
	}
	
}
