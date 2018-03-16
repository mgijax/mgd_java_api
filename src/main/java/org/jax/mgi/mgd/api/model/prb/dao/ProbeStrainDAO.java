package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

public class ProbeStrainDAO extends PostgresSQLDAO<ProbeStrain> {
	protected ProbeStrainDAO() {
		super(ProbeStrain.class);
	}
}
