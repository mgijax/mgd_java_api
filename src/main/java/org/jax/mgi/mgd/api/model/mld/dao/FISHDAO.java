package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.FISH;

@RequestScoped
public class FISHDAO extends PostgresSQLDAO<FISH> {

	protected FISHDAO() {
		super(FISH.class);
	}


}
