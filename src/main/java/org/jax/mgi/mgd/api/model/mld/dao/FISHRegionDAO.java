package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.FISHRegion;

@RequestScoped
public class FISHRegionDAO extends PostgresSQLDAO<FISHRegion> {

	protected FISHRegionDAO() {
		super(FISHRegion.class);
	}


}
