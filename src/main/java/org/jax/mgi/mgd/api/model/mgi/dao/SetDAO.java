package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISet;

@RequestScoped
public class SetDAO extends PostgresSQLDAO<MGISet> {

	public SetDAO() {
		super(MGISet.class);
	}

}
