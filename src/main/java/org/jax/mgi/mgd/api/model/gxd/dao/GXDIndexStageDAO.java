package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndexStage;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GXDIndexStageDAO extends PostgresSQLDAO<GXDIndexStage> {
	protected GXDIndexStageDAO() {
		super(GXDIndexStage.class);
	}
}
