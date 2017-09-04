package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.MGIType;

public class MGITypeDAO extends PostgresSQLDAO<MGIType> {
	protected MGITypeDAO() {
		super(MGIType.class);
	}
}
