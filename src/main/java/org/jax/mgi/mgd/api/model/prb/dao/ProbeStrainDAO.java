package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

@RequestScoped
public class ProbeStrainDAO extends PostgresSQLDAO<ProbeStrain> {

	protected ProbeStrainDAO() {
		super(ProbeStrain.class);
	}


}
