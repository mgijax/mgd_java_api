package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GelLaneService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/gellane")
@Api(value = "GelLane Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GelLaneController extends BaseController<GelLaneDomain> {

	@Inject
	private GelLaneService gellaneService;

	@Override
	public SearchResults<GelLaneDomain> create(GelLaneDomain gellane, User user) {
		return gellaneService.create(gellane, user);
	}

	@Override
	public SearchResults<GelLaneDomain> update(GelLaneDomain gellane, User user) {
		return gellaneService.update(gellane, user);
	}

	@Override
	public GelLaneDomain get(Integer gellaneKey) {
		return gellaneService.get(gellaneKey);
	}

	@Override
	public SearchResults<GelLaneDomain> delete(Integer key, User user) {
		return gellaneService.delete(key, user);
	}
	
}
