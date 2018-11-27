package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIRefAssocTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.MGIRefAssocTypeSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.MGIRefAssocTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgiRefAssocType")
@Api(value = "MGI Referene Assoc Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGIRefAssocTypeController extends BaseController<MGIRefAssocTypeDomain> {

	@Inject
	private MGIRefAssocTypeService refAssocTypeService;

	@Override
	public SearchResults<MGIRefAssocTypeDomain> create(MGIRefAssocTypeDomain event, User user) {
		return refAssocTypeService.create(event, user);
	}

	@Override
	public SearchResults<MGIRefAssocTypeDomain> update(MGIRefAssocTypeDomain event, User user) {
		return refAssocTypeService.update(event, user);
	}

	@Override
	public MGIRefAssocTypeDomain get(Integer key) {
		return refAssocTypeService.get(key);
	}

	@Override
	public SearchResults<MGIRefAssocTypeDomain> delete(Integer key, User user) {
		return refAssocTypeService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<MGIRefAssocTypeDomain> search(MGIRefAssocTypeSearchForm searchForm) {
		return refAssocTypeService.search(searchForm);
	}
	
}
