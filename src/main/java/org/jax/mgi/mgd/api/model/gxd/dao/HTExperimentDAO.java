package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HTExperimentDAO extends PostgresSQLDAO<HTExperiment> {
	protected HTExperimentDAO() {
		super(HTExperiment.class);
	}
}
