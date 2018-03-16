package org.jax.mgi.mgd.api.model.acc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.search.MGITypeSearchForm;
import org.jax.mgi.mgd.api.model.acc.service.MGITypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/mgitype")
@Api(value = "MGI Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGITypeController extends BaseController<MGITypeDomain> implements BaseSearchInterface<MGITypeDomain, MGITypeSearchForm> {

	@Inject
	private MGITypeService mgitypeService;

	public MGITypeDomain create(MGITypeDomain mgitype, User user) {
		try {
			return mgitypeService.create(mgitype, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public MGITypeDomain get(Integer key) {
		return mgitypeService.get(key);
	}

	public MGITypeDomain update(MGITypeDomain mgitype, User user) {
		return mgitypeService.update(mgitype, user);
	}

	public MGITypeDomain delete(Integer mgitype_key, User user) {
		return mgitypeService.delete(mgitype_key, user);
	}

	@Override
	public SearchResults<MGITypeDomain> search(MGITypeSearchForm searchForm) {
		return mgitypeService.search(searchForm);
	}
	
}
