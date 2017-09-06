package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.ActualDB;

@RequestScoped
public class ActualDBDAO extends PostgresSQLDAO<ActualDB> {

	protected ActualDBDAO() {
		super(ActualDB.class);
	}

}
