package org.jax.mgi.mgd.api.model.acc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.model.acc.search.ActualDBSearchForm;
import org.jax.mgi.mgd.api.model.acc.service.ActualDBService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/actualdb")
@Api(value = "ActualDB Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActualDBController extends BaseController<ActualDBDomain> {

	@Inject
	private ActualDBService actualdbService;

	@Override
	public SearchResults<ActualDBDomain> create(ActualDBDomain actualdb, User user) {
		return actualdbService.create(actualdb, user);
	}

	@Override
	public SearchResults<ActualDBDomain> update(ActualDBDomain actualdb, User user) {
		return actualdbService.update(actualdb, user);
	}

	@Override
	public ActualDBDomain get(Integer key) {
		return actualdbService.get(key);
	}

	@Override
	public SearchResults<ActualDBDomain> delete(Integer key, User user) {
		return actualdbService.delete(key, user);
	}

}
