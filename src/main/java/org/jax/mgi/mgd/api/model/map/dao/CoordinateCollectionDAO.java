package org.jax.mgi.mgd.api.model.map.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateCollection;

@RequestScoped
public class CoordinateCollectionDAO extends PostgresSQLDAO<CoordinateCollection> {

	public CoordinateCollectionDAO() {
		super(CoordinateCollection.class);
	}

}
