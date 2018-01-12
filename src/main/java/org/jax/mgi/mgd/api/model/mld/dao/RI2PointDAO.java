package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.RI2Point;

@RequestScoped
public class RI2PointDAO extends PostgresSQLDAO<RI2Point> {

	protected RI2PointDAO() {
		super(RI2Point.class);
	}


}
