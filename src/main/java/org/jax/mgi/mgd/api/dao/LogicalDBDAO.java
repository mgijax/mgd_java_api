package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.LogicalDB;

@RequestScoped
public class LogicalDBDAO extends PostgresSQLDAO<LogicalDB> {

	protected LogicalDBDAO() {
		super(LogicalDB.class);
	}

}
