package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ISRegion;

@RequestScoped
public class ISRegionDAO extends PostgresSQLDAO<ISRegion> {

	protected ISRegionDAO() {
		super(ISRegion.class);
	}


}
