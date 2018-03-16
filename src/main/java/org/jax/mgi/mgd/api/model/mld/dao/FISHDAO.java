package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.FISH;

public class FISHDAO extends PostgresSQLDAO<FISH> {
	protected FISHDAO() {
		super(FISH.class);
	}
}
