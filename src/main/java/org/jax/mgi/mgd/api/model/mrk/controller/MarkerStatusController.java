package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerStatusDomain;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerStatusService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/markerStatus")
@Tag(name = "Marker Status Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerStatusController extends BaseController<MarkerStatusDomain> {

	@Inject
	private MarkerStatusService markerStatusService;

	@Override
	public SearchResults<MarkerStatusDomain> create(MarkerStatusDomain markerStatus, User user) {
		return markerStatusService.create(markerStatus, user);
	}

	@Override
	public SearchResults<MarkerStatusDomain> update(MarkerStatusDomain markerStatus, User user) {
		return markerStatusService.update(markerStatus, user);
	}

	@Override
	public MarkerStatusDomain get(Integer key) {
		return markerStatusService.get(key);
	}

	@Override
	public SearchResults<MarkerStatusDomain> delete(Integer key, User user) {
		return markerStatusService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<MarkerStatusDomain> search() {
		return markerStatusService.search();
	}
	
}
