package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.MGISynonymTypeSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgiSynonymType")
@Api(value = "MGI Synonym Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISynonymTypeController extends BaseController<MGISynonymTypeDomain> {

	@Inject
	private MGISynonymTypeService synonymTypeService;

	@Override
	public SearchResults<MGISynonymTypeDomain> create(MGISynonymTypeDomain event, User user) {
		return synonymTypeService.create(event, user);
	}

	@Override
	public SearchResults<MGISynonymTypeDomain> update(MGISynonymTypeDomain event, User user) {
		return synonymTypeService.update(event, user);
	}

	@Override
	public MGISynonymTypeDomain get(Integer key) {
		return synonymTypeService.get(key);
	}

	@Override
	public SearchResults<MGISynonymTypeDomain> delete(Integer key, User user) {
		return synonymTypeService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<MGISynonymTypeDomain> search(MGISynonymTypeSearchForm searchForm) {
		return synonymTypeService.search(searchForm);
	}
	
}
