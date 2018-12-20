package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VariantType;

public class VariantTypeDAO extends PostgresSQLDAO<VariantType> {
	protected VariantTypeDAO() {
		super(VariantType.class);
	}
}
