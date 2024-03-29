package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISet;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGISetDAO extends PostgresSQLDAO<MGISet> {
	public MGISetDAO() {
		super(MGISet.class);
	}
}
