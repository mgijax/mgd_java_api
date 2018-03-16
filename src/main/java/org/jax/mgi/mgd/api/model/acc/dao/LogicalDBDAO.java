package org.jax.mgi.mgd.api.model.acc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;

public class LogicalDBDAO extends PostgresSQLDAO<LogicalDB> {
	protected LogicalDBDAO() {
		super(LogicalDB.class);
	}
}
