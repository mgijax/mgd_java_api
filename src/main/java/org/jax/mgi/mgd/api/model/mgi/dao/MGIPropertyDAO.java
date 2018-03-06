package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;

public class MGIPropertyDAO extends PostgresSQLDAO<MGIProperty> {
	public MGIPropertyDAO() {
		super(MGIProperty.class);
	}
}
