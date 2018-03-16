package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Experiment;

public class ExperimentDAO extends PostgresSQLDAO<Experiment> {
	protected ExperimentDAO() {
		super(Experiment.class);
	}
}
