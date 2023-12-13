package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIKeyValue;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGIKeyValueDAO extends PostgresSQLDAO<MGIKeyValue> {
	public MGIKeyValueDAO() {
		super(MGIKeyValue.class);
	}
}
