package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.Cross;

@RequestScoped
public class CrossDAO extends PostgresSQLDAO<Cross> {

	public CrossDAO() {
		super(Cross.class);
	}

}
