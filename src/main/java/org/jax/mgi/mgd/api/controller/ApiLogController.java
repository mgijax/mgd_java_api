package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.rest.interfaces.ApiLogRESTInterface;
import org.jax.mgi.mgd.api.service.ApiLogService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ApiLogController extends BaseController<ApiLogDomain> {

	@Inject
	private ApiLogService apiLogService;
	
	@Override
	public ApiLogDomain get(Integer key) {
		try {
			return apiLogService.get(key);
		} catch (APIException e) {
			return null;
		}
	}

	@Override
	public SearchResults<ApiLogDomain> search(Map<String, Object> postParams) {
		return apiLogService.search(postParams);
	}

	@Override
	public ApiLogDomain create(ApiLogDomain object) {
		//return apiLogService.create(endpoint, parameters, mgitype, objectKeys, user);
		return null;
	}

	@Override
	public ApiLogDomain update(ApiLogDomain object) {
		return null;
	}

	@Override
	public ApiLogDomain delete(Integer key) {
		return null;
	}
}
