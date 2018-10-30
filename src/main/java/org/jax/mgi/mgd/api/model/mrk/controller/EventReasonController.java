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
import org.jax.mgi.mgd.api.model.mrk.domain.EventReasonDomain;
import org.jax.mgi.mgd.api.model.mrk.search.EventReasonSearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.EventReasonService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerEventReason")
@Api(value = "Marker Event Reason Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventReasonController extends BaseController<EventReasonDomain> {

	@Inject
	private EventReasonService eventReasonService;

	@Override
	public EventReasonDomain create(EventReasonDomain eventReason, User user) {
		try {
			return eventReasonService.create(eventReason, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public EventReasonDomain update(EventReasonDomain eventReason, User user) {
		return eventReasonService.update(eventReason, user);
	}

	@Override
	public EventReasonDomain get(Integer key) {
		return eventReasonService.get(key);
	}

	@Override
	public SearchResults<EventReasonDomain> delete(Integer key, User user) {
		return eventReasonService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<EventReasonDomain> search(EventReasonSearchForm searchForm) {
		return eventReasonService.search(searchForm);
	}
	
}
