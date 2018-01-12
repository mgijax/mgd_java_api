package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.InSitu;

@RequestScoped
public class InSituDAO extends PostgresSQLDAO<InSitu> {

	protected InSituDAO() {
		super(InSitu.class);
	}


}
