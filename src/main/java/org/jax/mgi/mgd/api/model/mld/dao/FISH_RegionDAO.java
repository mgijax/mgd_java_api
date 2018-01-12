package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.FISH_Region;

@RequestScoped
public class FISH_RegionDAO extends PostgresSQLDAO<FISH_Region> {

	protected FISH_RegionDAO() {
		super(FISH_Region.class);
	}


}
