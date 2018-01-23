package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Hit;

@RequestScoped
public class HitDAO extends PostgresSQLDAO<Hit> {

	protected HitDAO() {
		super(Hit.class);
	}


}
