package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.dao.ActualDBDAO;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;

@RequestScoped
public class ActualDBService extends ServiceInterface<ActualDB> {

	@Inject
	private ActualDBDAO actualdbDAO;
	
	@Override
	public PostgresSQLDAO<ActualDB> getDAO() {
		return actualdbDAO;
	}

}
