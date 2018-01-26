package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDAssayType;

@RequestScoped
public class MLDAssayTypeDAO extends PostgresSQLDAO<MLDAssayType> {

	public MLDAssayTypeDAO() {
		super(MLDAssayType.class);
	}

}
