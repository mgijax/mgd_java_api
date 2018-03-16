package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelUnit;

public class GelUnitDAO extends PostgresSQLDAO<GelUnit> {
	protected GelUnitDAO() {
		super(GelUnit.class);
	}
}
