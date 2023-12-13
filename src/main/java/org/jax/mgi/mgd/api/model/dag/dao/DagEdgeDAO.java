package org.jax.mgi.mgd.api.model.dag.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.DagEdge;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DagEdgeDAO extends PostgresSQLDAO<DagEdge> {
	protected DagEdgeDAO() {
		super(DagEdge.class);
	}
}
