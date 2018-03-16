package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDLabel;

public class GXDLabelDAO extends PostgresSQLDAO<GXDLabel> {
	protected GXDLabelDAO() {
		super(GXDLabel.class);
	}
}
