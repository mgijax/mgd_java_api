package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VariantEffect;

public class VariantEffectDAO extends PostgresSQLDAO<VariantEffect> {
	protected VariantEffectDAO() {
		super(VariantEffect.class);
	}
}
