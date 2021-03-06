package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerType")
@Api(value = "Marker Type Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<MarkerTypeDomain> search() {
		return markerTypeService.search();
	}
	
}
