package org.jax.mgi.mgd.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.repository.ApiLogRepository;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ApiLogService {
	
	@Inject
	private ApiLogRepository repo;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	public ApiLogDomain get(Integer key) throws APIException {
		return repo.get(key);
	}
	
	public SearchResults<ApiLogDomain> search(Map<String, Object> searchFields) {
		return repo.search(searchFields);
	}
	
	@Transactional
	public ApiLogDomain create(String endpoint, String parameters, String mgitype, List<Integer> objectKeys, User user) throws APIException {
		ApiLogDomain domain = new ApiLogDomain();
		domain._event_key = null;					// repo will assign next key
		domain.endpoint = endpoint;
		domain.parameters = parameters;
		domain.mgitype = mgitype;
		domain.username = user.getLogin();
		domain.objectKeys = objectKeys;
		domain.creation_date = dateFormatter.format(new Date());
		return repo.create(domain, user);
	}
}
