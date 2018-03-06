package org.jax.mgi.mgd.api.model.acc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.SearchInterface;
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
public class ActualDBController extends BaseController<ActualDBDomain> implements SearchInterface<ActualDBDomain, ActualDBSearchForm> {

	@Inject
	private ActualDBService actualdbService;

	public ActualDBDomain create(ActualDBDomain actualdb, User user) {
		try {
			return actualdbService.create(actualdb, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ActualDBDomain update(ActualDBDomain actualdb, User user) {
		return actualdbService.update(actualdb, user);
	}

	public ActualDBDomain get(Integer key) {
		return actualdbService.get(key);
	}

	public ActualDBDomain delete(Integer actualdb_key, User user) {
		return actualdbService.delete(actualdb_key, user);
	}

	@Override
	public SearchResults<ActualDBDomain> search(ActualDBSearchForm form) {
		return actualdbService.search(form);
	}

}
