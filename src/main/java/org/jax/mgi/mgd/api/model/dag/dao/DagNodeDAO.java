package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.Node;

@RequestScoped
public class NodeDAO extends PostgresSQLDAO<Node> {

	protected NodeDAO() {
		super(Node.class);
	}


}
