package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/markerType")
@Tag(name = "Marker Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerTypeController extends BaseController<MarkerTypeDomain> {

	@Inject
	private MarkerTypeService markerTypeService;

	@Override
	public SearchResults<MarkerTypeDomain> create(MarkerTypeDomain markerType, User user) {
		return markerTypeService.create(markerType, user);
	}

	@Override
	public SearchResults<MarkerTypeDomain> update(MarkerTypeDomain markerType, User user) {
		return markerTypeService.update(markerType, user);
	}

	@Override
	public MarkerTypeDomain get(Integer key) {
		return markerTypeService.get(key);
	}

	@Override
	public SearchResults<MarkerTypeDomain> delete(Integer key, User user) {
		return markerTypeService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<MarkerTypeDomain> search() {
		return markerTypeService.search();
	}
	
}
