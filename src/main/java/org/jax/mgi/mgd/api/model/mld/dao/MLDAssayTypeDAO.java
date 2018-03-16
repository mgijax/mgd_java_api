package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDAssayType;

public class MLDAssayTypeDAO extends PostgresSQLDAO<MLDAssayType> {
	public MLDAssayTypeDAO() {
		super(MLDAssayType.class);
	}
}
