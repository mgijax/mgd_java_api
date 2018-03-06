package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.IndexStage;

public class IndexStageDAO extends PostgresSQLDAO<IndexStage> {
	protected IndexStageDAO() {
		super(IndexStage.class);
	}
}
