package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerTypeSearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerTypeService;

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
	public MarkerTypeDomain create(MarkerTypeDomain markerType, User user) {
		try {
			return markerTypeService.create(markerType, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MarkerTypeDomain update(MarkerTypeDomain markerType, User user) {
		return markerTypeService.update(markerType, user);
	}

	@Override
	public MarkerTypeDomain get(Integer markerTypeKey) {
		return markerTypeService.get(markerTypeKey);
	}

	@Override
	public MarkerTypeDomain delete(Integer key, User user) {
		return markerTypeService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Marker Type Search")
	@Path("/search")
	public List<MarkerTypeDomain> search(MarkerTypeSearchForm searchForm) {
		return markerTypeService.search(searchForm);
	}
	
}
