package org.jax.mgi.mgd.api.model.map.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.CoordCollection;

@RequestScoped
public class CoordCollectionDAO extends PostgresSQLDAO<CoordCollection> {

	public CoordCollectionDAO() {
		super(CoordCollection.class);
	}

}
