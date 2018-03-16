package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class MarkerDAO extends PostgresSQLDAO<Marker> {
	protected MarkerDAO() {
		super(Marker.class);
	}
}
