package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Experiment;

@RequestScoped
public class ExperimentDAO extends PostgresSQLDAO<Experiment> {

	protected ExperimentDAO() {
		super(Experiment.class);
	}


}
