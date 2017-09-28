package org.jax.mgi.mgd.api.dao;

import org.jax.mgi.mgd.api.entities.ApiLogEvent;

public class ApiLogDAO extends PostgresSQLDAO<ApiLogEvent> {

	protected ApiLogDAO() {
		super(ApiLogEvent.class);
	}

}
