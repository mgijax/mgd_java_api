package org.jax.mgi.mgd.api.model.acc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ActualDBDAO extends PostgresSQLDAO<ActualDB> {
	protected ActualDBDAO() {
		super(ActualDB.class);
	}
}
