package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.search.GelLaneSearchForm;
import org.jax.mgi.mgd.api.model.gxd.service.GelLaneService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/gellane")
@Api(value = "GelLane Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GelLaneController extends BaseController<GelLaneDomain> implements BaseSearchInterface<GelLaneDomain, GelLaneSearchForm> {

	@Inject
	private GelLaneService gellaneService;

	public GelLaneDomain create(GelLaneDomain gellane, User user) {
		try {
			return gellaneService.create(gellane, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public GelLaneDomain update(GelLaneDomain gellane, User user) {
		return gellaneService.update(gellane, user);
	}

	public GelLaneDomain get(Integer gellaneKey) {
		return gellaneService.get(gellaneKey);
	}

	public GelLaneDomain delete(Integer key, User user) {
		return gellaneService.delete(key, user);
	}
	
	@Override
	public SearchResults<GelLaneDomain> search(GelLaneSearchForm searchForm) {
		return gellaneService.search(searchForm);
	}

}
