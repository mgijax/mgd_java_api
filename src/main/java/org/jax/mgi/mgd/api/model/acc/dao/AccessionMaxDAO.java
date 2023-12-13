package org.jax.mgi.mgd.api.model.acc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.AccessionMax;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessionMaxDAO extends PostgresSQLDAO<AccessionMax> {
	protected AccessionMaxDAO() {
		super(AccessionMax.class);
	}
}
