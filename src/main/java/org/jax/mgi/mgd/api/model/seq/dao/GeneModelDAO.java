package org.jax.mgi.mgd.api.model.seq.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.GeneModel;

@RequestScoped
public class GeneModelDAO extends PostgresSQLDAO<GeneModel> {

	protected GeneModelDAO() {
		super(GeneModel.class);
	}


}
