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
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerHistorySearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerHistoryService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerHistory")
@Api(value = "Marker History Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<MarkerHistoryDomain> search(MarkerHistorySearchForm searchForm) {
		return markerHistoryService.search(searchForm);
	}
	
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public void processHistory(String parentKey, List<MarkerHistoryDomain> domain, User user) {
		markerHistoryService.processHistory(parentKey, domain, user);
		return;
	}
	
}
