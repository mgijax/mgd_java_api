package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.DatabaseInfo;

@RequestScoped
public class DatabaseInfoDAO extends PostgresSQLDAO<DatabaseInfo> {

	public DatabaseInfoDAO() {
		super(DatabaseInfo.class);
	}

}
