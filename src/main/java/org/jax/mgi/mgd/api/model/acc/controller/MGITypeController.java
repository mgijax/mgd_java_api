package org.jax.mgi.mgd.api.model.acc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimMGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.service.MGITypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgitype")
@Api(value = "MGI Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGITypeController extends BaseController<MGITypeDomain> {

	@Inject
	private MGITypeService mgitypeService;

	@Override
	public SearchResults<MGITypeDomain> create(MGITypeDomain mgitype, User user) {
		return mgitypeService.create(mgitype, user);
	}

	@Override
	public SearchResults<MGITypeDomain> update(MGITypeDomain mgitype, User user) {
		return mgitypeService.update(mgitype, user);
	}
	
	@Override
	public MGITypeDomain get(Integer key) {
		return mgitypeService.get(key);
	}

	@Override
	public SearchResults<MGITypeDomain> delete(Integer key, User user) {
		return mgitypeService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search")
	@Path("/search")	
	public SearchResults<SlimMGITypeDomain> search(SlimMGITypeDomain searchDomain) {
		return mgitypeService.search(searchDomain);
	}
		
}
