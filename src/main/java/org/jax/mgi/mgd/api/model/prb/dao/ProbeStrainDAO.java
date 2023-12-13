package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeStrainDAO extends PostgresSQLDAO<ProbeStrain> {
	protected ProbeStrainDAO() {
		super(ProbeStrain.class);
	}
}
