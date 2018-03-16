package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.DatabaseInfo;

public class DatabaseInfoDAO extends PostgresSQLDAO<DatabaseInfo> {
	public DatabaseInfoDAO() {
		super(DatabaseInfo.class);
	}
}
