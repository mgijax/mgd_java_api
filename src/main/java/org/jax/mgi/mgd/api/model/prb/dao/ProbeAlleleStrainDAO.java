package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAlleleStrain;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeAlleleStrainDAO extends PostgresSQLDAO<ProbeAlleleStrain> {
	protected ProbeAlleleStrainDAO() {
		super(ProbeAlleleStrain.class);
	}
}
