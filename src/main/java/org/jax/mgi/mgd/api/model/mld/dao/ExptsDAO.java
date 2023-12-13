package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExptsDAO extends PostgresSQLDAO<Expts> {
	protected ExptsDAO() {
		super(Expts.class);
	}
}
