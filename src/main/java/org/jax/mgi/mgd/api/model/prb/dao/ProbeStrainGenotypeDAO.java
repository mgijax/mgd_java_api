package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainGenotype;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeStrainGenotypeDAO extends PostgresSQLDAO<ProbeStrainGenotype> {
	protected ProbeStrainGenotypeDAO() {
		super(ProbeStrainGenotype.class);
	}
}
