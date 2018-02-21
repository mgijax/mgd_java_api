package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.service.ActualDBService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ActualDBController extends BaseController<ActualDBDomain> {

	@Inject
	private ActualDBService actualdbService;

	@Override
	public ActualDBDomain create(ActualDBDomain actualdb) {
		return actualdbService.create(actualdb);
	}

	@Override
	public ActualDBDomain update(ActualDBDomain actualdb) {
		return actualdbService.update(actualdb);
	}

	@Override
	public ActualDBDomain get(Integer key) {
		return actualdbService.get(key);
	}

	@Override
	public ActualDBDomain delete(Integer actualdb_key) {
		return actualdbService.delete(actualdb_key);
	}

	@Override
	public SearchResults<ActualDBDomain> search(Map<String, Object> postParams) {
		return actualdbService.search(postParams);

	}

}
