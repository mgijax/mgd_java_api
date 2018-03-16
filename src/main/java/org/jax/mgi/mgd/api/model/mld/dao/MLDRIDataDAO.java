package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDRIData;

public class MLDRIDataDAO extends PostgresSQLDAO<MLDRIData> {
	protected MLDRIDataDAO() {
		super(MLDRIData.class);
	}
}
