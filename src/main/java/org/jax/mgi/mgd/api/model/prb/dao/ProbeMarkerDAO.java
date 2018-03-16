package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;

public class ProbeMarkerDAO extends PostgresSQLDAO<ProbeMarker> {
	protected ProbeMarkerDAO() {
		super(ProbeMarker.class);
	}
}
