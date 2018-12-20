package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VarEffect;

public class VarEffectDAO extends PostgresSQLDAO<VarEffect> {
	protected VarEffectDAO() {
		super(VarEffect.class);
	}
}
