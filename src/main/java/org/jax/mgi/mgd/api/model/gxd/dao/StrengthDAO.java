package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Strength;

public class StrengthDAO extends PostgresSQLDAO<Strength> {
	protected StrengthDAO() {
		super(Strength.class);
	}
}
