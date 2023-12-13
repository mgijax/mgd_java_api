package org.jax.mgi.mgd.api.model.dag.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.Dag;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DagDAO extends PostgresSQLDAO<Dag> {
	protected DagDAO() {
		super(Dag.class);
	}
}
