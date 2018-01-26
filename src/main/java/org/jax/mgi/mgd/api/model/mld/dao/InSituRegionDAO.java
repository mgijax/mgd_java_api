package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.InSituRegion;

@RequestScoped
public class InSituRegionDAO extends PostgresSQLDAO<InSituRegion> {

	protected InSituRegionDAO() {
		super(InSituRegion.class);
	}


}
