package org.jax.mgi.mgd.api.model.acc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.AccessionMax;

@RequestScoped
public class AccessionMaxDAO extends PostgresSQLDAO<AccessionMax> {

	protected AccessionMaxDAO() {
		super(AccessionMax.class);
	}

}
