package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;

@RequestScoped
public class ExptsDAO extends PostgresSQLDAO<Expts> {

	protected ExptsDAO() {
		super(Expts.class);
	}


}
