package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.TheilerStage;

@RequestScoped
public class TheilerStageDAO extends PostgresSQLDAO<TheilerStage> {

	protected TheilerStageDAO() {
		super(TheilerStage.class);
	}


}
