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
import org.jax.mgi.mgd.api.model.mrk.domain.EventDomain;
import org.jax.mgi.mgd.api.model.mrk.search.EventSearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.EventService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerEvent")
@Api(value = "Marker Event Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventController extends BaseController<EventDomain> {

	@Inject
	private EventService eventService;

	@Override
	public SearchResults<EventDomain> create(EventDomain event, User user) {
		return eventService.create(event, user);
	}

	@Override
	public SearchResults<EventDomain> update(EventDomain event, User user) {
		return eventService.update(event, user);
	}

	@Override
	public EventDomain get(Integer key) {
		return eventService.get(key);
	}

	@Override
	public SearchResults<EventDomain> delete(Integer key, User user) {
		return eventService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<EventDomain> search(EventSearchForm searchForm) {
		return eventService.search(searchForm);
	}
	
}
