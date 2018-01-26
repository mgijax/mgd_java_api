package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDStatistic;

@RequestScoped
public class MLDStatisticDAO extends PostgresSQLDAO<MLDStatistic> {

	protected MLDStatisticDAO() {
		super(MLDStatistic.class);
	}


}
