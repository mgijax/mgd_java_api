package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerStrain;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarkerStrainDAO extends PostgresSQLDAO<MarkerStrain> {
	protected MarkerStrainDAO() {
		super(MarkerStrain.class);
	}
}
