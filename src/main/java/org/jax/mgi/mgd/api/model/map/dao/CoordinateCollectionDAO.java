package org.jax.mgi.mgd.api.model.map.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateCollection;

public class CoordinateCollectionDAO extends PostgresSQLDAO<CoordinateCollection> {
	public CoordinateCollectionDAO() {
		super(CoordinateCollection.class);
	}
}
