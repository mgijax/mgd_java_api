package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GelLaneDAO extends PostgresSQLDAO<GelLane> {
	protected GelLaneDAO() {
		super(GelLane.class);
	}
}
