package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.DatabaseInfo;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseInfoDAO extends PostgresSQLDAO<DatabaseInfo> {
	public DatabaseInfoDAO() {
		super(DatabaseInfo.class);
	}
}
