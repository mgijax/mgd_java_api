package org.jax.mgi.mgd.api.model.crs.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.Cross;

public class CrossDAO extends PostgresSQLDAO<Cross> {
	public CrossDAO() {
		super(Cross.class);
	}
}
