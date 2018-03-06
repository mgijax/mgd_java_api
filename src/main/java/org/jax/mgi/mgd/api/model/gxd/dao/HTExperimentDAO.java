package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;

public class HTExperimentDAO extends PostgresSQLDAO<HTExperiment> {
	protected HTExperimentDAO() {
		super(HTExperiment.class);
	}
}
