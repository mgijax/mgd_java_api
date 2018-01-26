package org.jax.mgi.mgd.api.model.map.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.CoordFeature;

@RequestScoped
public class CoordFeatureDAO extends PostgresSQLDAO<CoordFeature> {

	public CoordFeatureDAO() {
		super(CoordFeature.class);
	}

}
