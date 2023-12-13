package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGIPropertyDAO extends PostgresSQLDAO<MGIProperty> {
	public MGIPropertyDAO() {
		super(MGIProperty.class);
	}
}
