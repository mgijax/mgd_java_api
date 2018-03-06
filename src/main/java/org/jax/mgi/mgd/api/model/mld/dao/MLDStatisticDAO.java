package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDStatistic;

public class MLDStatisticDAO extends PostgresSQLDAO<MLDStatistic> {
	protected MLDStatisticDAO() {
		super(MLDStatistic.class);
	}
}
