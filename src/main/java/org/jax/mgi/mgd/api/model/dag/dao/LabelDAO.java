package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagLabel;

@RequestScoped
public class LabelDAO extends PostgresSQLDAO<DagLabel> {

	protected LabelDAO() {
		super(DagLabel.class);
	}


}
