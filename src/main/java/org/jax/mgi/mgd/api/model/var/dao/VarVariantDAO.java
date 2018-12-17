package org.jax.mgi.mgd.api.model.var.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.var.entities.VarVariant;

public class VarVariantDAO extends PostgresSQLDAO<VarVariant> {
	protected VarVariantDAO() {
		super(VarVariant.class);
	}
}