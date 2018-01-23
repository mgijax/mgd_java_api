package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.IndexStage;

@RequestScoped
public class IndexStageDAO extends PostgresSQLDAO<IndexStage> {

	protected IndexStageDAO() {
		super(IndexStage.class);
	}


}
