package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagNode;

@RequestScoped
public class DagNodeDAO extends PostgresSQLDAO<DagNode> {

	protected DagNodeDAO() {
		super(DagNode.class);
	}


}
