package org.jax.mgi.mgd.api.service;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.ApiEventLogDAO;
import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.entities.ApiEventLog;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ApiEventLogService extends ServiceInterface<ApiEventLog> {
	
	@Inject
	private ApiEventLogDAO apiEventLogDAO;
	
	private Logger log = Logger.getLogger(getClass());
	
	@Override
	public PostgresSQLDAO<ApiEventLog> getDAO() {
		return apiEventLogDAO;
	}

	public void create(String endpoint, DomainBase model) {

		try {
			ApiEventLog log = new ApiEventLog(0, endpoint, mapper.writeValueAsString(model));
			apiEventLogDAO.create(log);
		} catch (JsonProcessingException e) {
			log.info("Error Occured not logging: ");
			e.printStackTrace();
		}
		
	}
	
}
