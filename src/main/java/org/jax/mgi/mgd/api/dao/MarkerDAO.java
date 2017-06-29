package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Marker;

@RequestScoped
public class MarkerDAO extends PostgresSQLDAO<Marker> {

	public MarkerDAO() {
		clazz = Marker.class;
	}


}
