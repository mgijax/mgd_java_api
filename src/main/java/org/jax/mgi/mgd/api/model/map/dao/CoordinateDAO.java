package org.jax.mgi.mgd.api.model.map.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;

@RequestScoped
public class CoordinateDAO extends PostgresSQLDAO<CoordinateDAO> {

	public CoordinateDAO() {
		super(CoordinateDAO.class);
	}

}
