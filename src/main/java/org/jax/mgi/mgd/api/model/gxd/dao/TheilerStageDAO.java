package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.TheilerStage;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TheilerStageDAO extends PostgresSQLDAO<TheilerStage> {
	protected TheilerStageDAO() {
		super(TheilerStage.class);
	}
}
