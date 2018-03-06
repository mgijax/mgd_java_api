package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.FISHRegion;

public class FISHRegionDAO extends PostgresSQLDAO<FISHRegion> {
	protected FISHRegionDAO() {
		super(FISHRegion.class);
	}
}
