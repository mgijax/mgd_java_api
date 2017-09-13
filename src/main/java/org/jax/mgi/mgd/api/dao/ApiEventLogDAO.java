package org.jax.mgi.mgd.api.dao;

import org.jax.mgi.mgd.api.entities.ApiEventLog;

public class ApiEventLogDAO extends PostgresSQLDAO<ApiEventLog> {

	protected ApiEventLogDAO() {
		super(ApiEventLog.class);
	}

}
