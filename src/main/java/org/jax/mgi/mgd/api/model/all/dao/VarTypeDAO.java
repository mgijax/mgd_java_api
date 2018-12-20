package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VarType;

public class VarTypeDAO extends PostgresSQLDAO<VarType> {
	protected VarTypeDAO() {
		super(VarType.class);
	}
}
