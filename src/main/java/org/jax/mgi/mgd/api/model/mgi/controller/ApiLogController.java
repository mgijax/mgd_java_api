package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.interfaces.ApiLogRESTInterface;
import org.jax.mgi.mgd.api.model.mgi.service.ApiLogService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ApiLogController extends BaseController<ApiLogDomain> implements ApiLogRESTInterface {

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

	public SearchResults<ApiLogDomain> search(Map<String, Object> postParams) {
		return apiLogService.search(postParams);
	}

	@Override
	public ApiLogDomain create(ApiLogDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiLogDomain update(ApiLogDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiLogDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}


}
