package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MC2point;

@RequestScoped
public class MC2pointDAO extends PostgresSQLDAO<MC2point> {

	protected MC2pointDAO() {
		super(MC2point.class);
	}


}
