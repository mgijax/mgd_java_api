package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResult;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InSituResultDAO extends PostgresSQLDAO<InSituResult> {
	protected InSituResultDAO() {
		super(InSituResult.class);
	}
}
