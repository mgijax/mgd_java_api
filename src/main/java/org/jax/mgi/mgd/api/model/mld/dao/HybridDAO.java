package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Hybrid;

public class HybridDAO extends PostgresSQLDAO<Hybrid> {
	protected HybridDAO() {
		super(Hybrid.class);
	}
}
