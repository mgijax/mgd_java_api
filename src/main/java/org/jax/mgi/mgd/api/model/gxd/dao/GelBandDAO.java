package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelBand;

public class GelBandDAO extends PostgresSQLDAO<GelBand> {
	protected GelBandDAO() {
		super(GelBand.class);
	}
}
