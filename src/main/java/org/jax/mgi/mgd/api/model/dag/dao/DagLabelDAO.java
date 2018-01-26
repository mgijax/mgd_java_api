package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagLabel;

@RequestScoped
public class DagLabelDAO extends PostgresSQLDAO<DagLabel> {

	protected DagLabelDAO() {
		super(DagLabel.class);
	}


}
