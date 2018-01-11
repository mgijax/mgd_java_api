package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.Edge;

@RequestScoped
public class EdgeDAO extends PostgresSQLDAO<Edge> {

	protected EdgeDAO() {
		super(Edge.class);
	}


}
