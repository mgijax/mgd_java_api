package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.InSitu;

public class InSituDAO extends PostgresSQLDAO<InSitu> {
	protected InSituDAO() {
		super(InSitu.class);
	}
}
