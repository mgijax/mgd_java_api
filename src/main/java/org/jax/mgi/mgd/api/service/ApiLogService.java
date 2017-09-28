package org.jax.mgi.mgd.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.dao.ApiLogDAO;
import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.entities.ApiLogEvent;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.repository.ApiLogRepository;

public class ApiLogService extends ServiceInterface<ApiLogEvent> {
	
	@Inject
	private ApiLogDAO apiLogDAO;
	
	@Inject
	private ApiLogRepository repo;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	@Override
	public PostgresSQLDAO<ApiLogEvent> getDAO() {
		return apiLogDAO;
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
