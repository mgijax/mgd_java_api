package org.jax.mgi.mgd.api.model.acc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;

@RequestScoped
public class ActualDBDAO extends PostgresSQLDAO<ActualDB> {

	protected ActualDBDAO() {
		super(ActualDB.class);
	}

}
