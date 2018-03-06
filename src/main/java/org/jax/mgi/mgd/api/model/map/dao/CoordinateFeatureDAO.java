package org.jax.mgi.mgd.api.model.map.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateFeature;

public class CoordinateFeatureDAO extends PostgresSQLDAO<CoordinateFeature> {
	public CoordinateFeatureDAO() {
		super(CoordinateFeature.class);
	}
}
