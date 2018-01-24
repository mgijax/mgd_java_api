package org.jax.mgi.mgd.api.model.map.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.map.entities.Coord_Collection;

@RequestScoped
public class Coord_CollectionDAO extends PostgresSQLDAO<Coord_Collection> {

	public Coord_CollectionDAO() {
		super(Coord_Collection.class);
	}

}
