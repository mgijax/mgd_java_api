package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.InSituRegion;

public class InSituRegionDAO extends PostgresSQLDAO<InSituRegion> {
	protected InSituRegionDAO() {
		super(InSituRegion.class);
	}
}
