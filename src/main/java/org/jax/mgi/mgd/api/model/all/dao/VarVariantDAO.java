package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VarVariant;

public class VarVariantDAO extends PostgresSQLDAO<VarVariant> {
	protected VarVariantDAO() {
		super(VarVariant.class);
	}
}
