package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainGenotype;

public class ProbeStrainGenotypeDAO extends PostgresSQLDAO<ProbeStrainGenotype> {
	protected ProbeStrainGenotypeDAO() {
		super(ProbeStrainGenotype.class);
	}
}
