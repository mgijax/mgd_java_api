package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Hit;

public class HitDAO extends PostgresSQLDAO<Hit> {
	protected HitDAO() {
		super(Hit.class);
	}
}
