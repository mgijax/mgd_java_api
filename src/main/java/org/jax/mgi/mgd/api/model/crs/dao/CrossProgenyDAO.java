package org.jax.mgi.mgd.api.model.crs.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossProgeny;

public class CrossProgenyDAO extends PostgresSQLDAO<CrossProgeny> {
	public CrossProgenyDAO() {
		super(CrossProgeny.class);
	}
}
