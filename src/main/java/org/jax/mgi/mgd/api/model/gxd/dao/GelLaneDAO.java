package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;

@RequestScoped
public class GelLaneDAO extends PostgresSQLDAO<GelLane> {

	protected GelLaneDAO() {
		super(GelLane.class);
	}


}
