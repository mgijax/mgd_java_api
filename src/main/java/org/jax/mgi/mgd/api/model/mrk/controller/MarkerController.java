package org.jax.mgi.mgd.api.model.mrk.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.SearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/marker")
@Api(value = "Marker Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerController extends BaseController<MarkerDomain> implements SearchInterface<MarkerDomain, MarkerSearchForm> {

	@Inject
	private MarkerService markerService;

	public MarkerDomain create(MarkerDomain marker, User user) {
		try {
			return markerService.create(marker, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public MarkerDomain update(MarkerDomain marker, User user) {
		return markerService.update(marker, user);
	}

	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	public MarkerDomain delete(Integer key, User user) {
		return markerService.delete(key, user);
	}
	
	@Override
	public SearchResults<MarkerDomain> search(MarkerSearchForm searchForm) {
		return markerService.search(searchForm);
	}

}
