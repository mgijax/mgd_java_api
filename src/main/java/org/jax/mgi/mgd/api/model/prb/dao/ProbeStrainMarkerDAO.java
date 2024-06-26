package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainMarker;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeStrainMarkerDAO extends PostgresSQLDAO<ProbeStrainMarker> {
	protected ProbeStrainMarkerDAO() {
		super(ProbeStrainMarker.class);
	}
}
