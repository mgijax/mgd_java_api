package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MC2Point;

public class MC2PointDAO extends PostgresSQLDAO<MC2Point> {
	protected MC2PointDAO() {
		super(MC2Point.class);
	}
}
