package org.jax.mgi.mgd.api.model.dag.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagNode;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DagNodeDAO extends PostgresSQLDAO<DagNode> {
	protected DagNodeDAO() {
		super(DagNode.class);
	}
}
