package org.jax.mgi.mgd.api.model.dag.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagLabel;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DagLabelDAO extends PostgresSQLDAO<DagLabel> {
	protected DagLabelDAO() {
		super(DagLabel.class);
	}
}
