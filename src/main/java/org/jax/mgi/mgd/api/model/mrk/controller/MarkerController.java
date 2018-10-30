package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.List;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIResultDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/marker")
@Api(value = "Marker Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerController extends BaseController<MarkerDomain> {

	@Inject
	private MarkerService markerService;

	@Override
	public MarkerDomain create(MarkerDomain marker, User user) {
		try {
			return markerService.create(marker, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public MarkerDomain update(MarkerDomain marker, User user) {
		return markerService.update(marker, user);
	}

	@Override
	public MarkerDomain get(Integer markerKey) {
		return markerService.get(markerKey);
	}

	@Override
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		return markerService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "EI Search")
	@Path("/eiSearch")
	public List<MarkerEIResultDomain> eiSearch(MarkerSearchForm searchForm) {
		return markerService.eiSearch(searchForm);
	}

	@POST
	@ApiOperation(value = "EI Utilities ")
	@Path("/eiUtilities")
	public MarkerEIUtilitiesDomain eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
		return markerService.eiUtilities(searchForm);
	}
	
}
