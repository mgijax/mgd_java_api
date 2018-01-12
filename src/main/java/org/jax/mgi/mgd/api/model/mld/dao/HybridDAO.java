package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Hybrid;

@RequestScoped
public class HybridDAO extends PostgresSQLDAO<Hybrid> {

	protected HybridDAO() {
		super(Hybrid.class);
	}


}
