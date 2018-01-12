package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.RIData;

@RequestScoped
public class RIDataDAO extends PostgresSQLDAO<RIData> {

	protected RIDataDAO() {
		super(RIData.class);
	}


}
