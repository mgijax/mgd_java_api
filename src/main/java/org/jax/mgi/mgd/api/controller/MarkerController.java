package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.MarkerDomain;
import org.jax.mgi.mgd.api.service.MarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/marker")
@Api(value = "Marker Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerController extends BaseController<MarkerDomain> {

	@Inject
	private MarkerService markerService;

	public MarkerDomain create(MarkerDomain marker) {
		return markerService.create(marker);
	}

	public MarkerDomain update(MarkerDomain marker) {
		return markerService.update(marker);
	}

	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	public MarkerDomain delete(Integer key) {
		return markerService.delete(key);
	}

	public SearchResults<MarkerDomain> search(Map<String, Object> postParams) {
		return markerService.search(postParams);
	}

}
