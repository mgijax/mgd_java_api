package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;

public class ExptMarkerDAO extends PostgresSQLDAO<ExptMarker> {
	protected ExptMarkerDAO() {
		super(ExptMarker.class);
	}
}
