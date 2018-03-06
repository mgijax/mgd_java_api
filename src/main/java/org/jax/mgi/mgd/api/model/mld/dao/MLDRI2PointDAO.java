package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDRI2Point;

public class MLDRI2PointDAO extends PostgresSQLDAO<MLDRI2Point> {
	protected MLDRI2PointDAO() {
		super(MLDRI2Point.class);
	}
}
