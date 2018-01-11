package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;

@RequestScoped
public class HTExperimentDAO extends PostgresSQLDAO<HTExperiment> {

	protected HTExperimentDAO() {
		super(HTExperiment.class);
	}


}
