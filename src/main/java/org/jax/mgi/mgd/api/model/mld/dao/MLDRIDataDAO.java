package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDRIData;

@RequestScoped
public class MLDRIDataDAO extends PostgresSQLDAO<MLDRIData> {

	protected MLDRIDataDAO() {
		super(MLDRIData.class);
	}


}
