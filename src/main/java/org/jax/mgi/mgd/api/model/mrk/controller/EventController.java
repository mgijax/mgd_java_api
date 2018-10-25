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
import org.jax.mgi.mgd.api.model.mrk.domain.EventDomain;
import org.jax.mgi.mgd.api.model.mrk.search.EventSearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.EventService;

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
	public EventDomain create(EventDomain event, User user) {
		try {
			return eventService.create(event, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public EventDomain update(EventDomain event, User user) {
		return eventService.update(event, user);
	}

	@Override
	public EventDomain get(Integer eventKey) {
		return eventService.get(eventKey);
	}

	@Override
	public EventDomain delete(Integer key, User user) {
		return eventService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<EventDomain> search(EventSearchForm searchForm) {
		return eventService.search(searchForm);
	}
	
}
