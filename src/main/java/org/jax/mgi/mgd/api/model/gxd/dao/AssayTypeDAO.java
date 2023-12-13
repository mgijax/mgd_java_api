package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AssayType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssayTypeDAO extends PostgresSQLDAO<AssayType> {
	protected AssayTypeDAO() {
		super(AssayType.class);
	}
}
