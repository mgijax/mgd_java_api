package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.dao.LogicalDBDAO;
import org.jax.mgi.mgd.api.entities.LogicalDB;

@RequestScoped
public class LogicalDBService extends ServiceInterface<LogicalDB> {

	@Inject
	private LogicalDBDAO logicaldbDAO;
	
	@Override
	public PostgresSQLDAO<LogicalDB> getDAO() {
		return logicaldbDAO;
	}

}
