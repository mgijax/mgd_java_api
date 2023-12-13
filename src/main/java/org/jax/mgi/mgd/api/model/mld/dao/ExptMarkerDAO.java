package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExptMarkerDAO extends PostgresSQLDAO<ExptMarker> {
	protected ExptMarkerDAO() {
		super(ExptMarker.class);
	}
}
