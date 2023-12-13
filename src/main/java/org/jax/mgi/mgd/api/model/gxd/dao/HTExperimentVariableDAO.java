package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperimentVariable;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HTExperimentVariableDAO extends PostgresSQLDAO<HTExperimentVariable> {
	protected HTExperimentVariableDAO() {
		super(HTExperimentVariable.class);
	}
}
