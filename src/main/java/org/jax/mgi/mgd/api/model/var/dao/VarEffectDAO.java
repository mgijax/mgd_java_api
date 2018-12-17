package org.jax.mgi.mgd.api.model.var.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.var.entities.VarEffect;

public class VarEffectDAO extends PostgresSQLDAO<VarEffect> {
	protected VarEffectDAO() {
		super(VarEffect.class);
	}
}