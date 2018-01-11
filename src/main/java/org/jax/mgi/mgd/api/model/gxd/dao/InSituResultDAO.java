package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResult;

@RequestScoped
public class InSituResultDAO extends PostgresSQLDAO<InSituResult> {

	protected InSituResultDAO() {
		super(InSituResult.class);
	}


}
