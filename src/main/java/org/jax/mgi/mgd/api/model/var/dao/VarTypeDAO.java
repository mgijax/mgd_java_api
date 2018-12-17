package org.jax.mgi.mgd.api.model.var.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.var.entities.VarType;

public class VarTypeDAO extends PostgresSQLDAO<VarType> {
	protected VarTypeDAO() {
		super(VarType.class);
	}
}