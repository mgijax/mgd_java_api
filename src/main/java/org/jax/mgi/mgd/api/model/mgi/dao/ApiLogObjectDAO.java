package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.ApiLogObject;

public class ApiLogObjectDAO extends PostgresSQLDAO<ApiLogObject> {
	protected ApiLogObjectDAO() {
		super(ApiLogObject.class);
	}
}
