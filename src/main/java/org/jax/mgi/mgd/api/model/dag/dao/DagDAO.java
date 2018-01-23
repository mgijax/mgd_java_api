package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.Dag;

@RequestScoped
public class DagDAO extends PostgresSQLDAO<Dag> {

	protected DagDAO() {
		super(Dag.class);
	}


}
