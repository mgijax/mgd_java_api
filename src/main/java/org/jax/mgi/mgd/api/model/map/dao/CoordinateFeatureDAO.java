package org.jax.mgi.mgd.api.model.map.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateFeature;

@RequestScoped
public class CoordinateFeatureDAO extends PostgresSQLDAO<CoordinateFeature> {

	public CoordinateFeatureDAO() {
		super(CoordinateFeature.class);
	}

}
