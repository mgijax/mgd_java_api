package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndex;

public class GXDIndexDAO extends PostgresSQLDAO<GXDIndex> {
	protected GXDIndexDAO() {
		super(GXDIndex.class);
	}
}
