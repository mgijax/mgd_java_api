package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Marker;
import org.yaml.snakeyaml.error.Mark;

@RequestScoped
public class MarkerDAO extends PostgresSQLDAO<Marker> {

	protected MarkerDAO() {
		super(Marker.class);
	}


}
