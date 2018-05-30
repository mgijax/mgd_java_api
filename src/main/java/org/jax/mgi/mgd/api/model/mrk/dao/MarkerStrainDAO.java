package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerStrain;

public class MarkerStrainDAO extends PostgresSQLDAO<MarkerStrain> {
	protected MarkerStrainDAO() {
		super(MarkerStrain.class);
	}
}
