package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VariantSequence;

public class VariantSequenceDAO extends PostgresSQLDAO<VariantSequence> {
	protected VariantSequenceDAO() {
		super(VariantSequence.class);
	}
}
