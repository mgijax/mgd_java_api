package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagEdge;

@RequestScoped
public class DagEdgeDAO extends PostgresSQLDAO<DagEdge> {

	protected DagEdgeDAO() {
		super(DagEdge.class);
	}


}
