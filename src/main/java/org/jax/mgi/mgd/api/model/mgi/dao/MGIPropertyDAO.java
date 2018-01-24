package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;

@RequestScoped
public class MGIPropertyDAO extends PostgresSQLDAO<MGIProperty> {

	public MGIPropertyDAO() {
		super(MGIProperty.class);
	}

}
