package org.jax.mgi.mgd.api.model.acc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;

public class MGITypeDAO extends PostgresSQLDAO<MGIType> {
	protected MGITypeDAO() {
		super(MGIType.class);
	}
}
