package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.service.MGITypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/mgitype")
@Api(value = "MGI Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGITypeController extends BaseController<MGITypeDomain> {

	@Inject
	private MGITypeService mgitypeService;

	public MGITypeDomain create(MGITypeDomain mgitype) {
		return mgitypeService.create(mgitype);
	}

	public MGITypeDomain getByKey(Integer key) {
		return mgitypeService.get(key);
	}

	public MGITypeDomain update(MGITypeDomain mgitype) {
		return mgitypeService.update(mgitype);
	}

	public MGITypeDomain delete(Integer mgitype_key) {
		return mgitypeService.delete(mgitype_key);
	}

	public SearchResults<MGITypeDomain> searchByFields(Map<String, Object> postParams) {
		return mgitypeService.search(postParams);
	}
	
}
