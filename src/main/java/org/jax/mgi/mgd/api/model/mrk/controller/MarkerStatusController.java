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
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerStatusDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerStatusSearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerStatusService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerStatus")
@Api(value = "Marker Status Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerStatusController extends BaseController<MarkerStatusDomain> {

	@Inject
	private MarkerStatusService markerStatusService;

	public SearchResults<MarkerStatusDomain> create(MarkerStatusDomain markerStatus, User user) {
		return markerStatusService.create(markerStatus, user);
	}

	public SearchResults<MarkerStatusDomain> update(MarkerStatusDomain markerStatus, User user) {
		return markerStatusService.update(markerStatus, user);
	}

	public MarkerStatusDomain get(Integer key) {
		return markerStatusService.get(key);
	}

	public SearchResults<MarkerStatusDomain> delete(Integer key, User user) {
		return markerStatusService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<MarkerStatusDomain> search(MarkerStatusSearchForm searchForm) {
		return markerStatusService.search(searchForm);
	}
	
}
