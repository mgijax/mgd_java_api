package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISet;

@RequestScoped
public class MGISetDAO extends PostgresSQLDAO<MGISet> {

	public MGISetDAO() {
		super(MGISet.class);
	}

}
