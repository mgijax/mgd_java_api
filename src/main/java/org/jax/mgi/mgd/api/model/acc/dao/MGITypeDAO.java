package org.jax.mgi.mgd.api.model.acc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGITypeDAO extends PostgresSQLDAO<MGIType> {
	protected MGITypeDAO() {
		super(MGIType.class);
	}
}
