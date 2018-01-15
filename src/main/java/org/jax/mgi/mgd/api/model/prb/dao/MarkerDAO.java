package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Marker;

@RequestScoped
public class MarkerDAO extends PostgresSQLDAO<Marker> {

	protected MarkerDAO() {
		super(Marker.class);
	}


}
