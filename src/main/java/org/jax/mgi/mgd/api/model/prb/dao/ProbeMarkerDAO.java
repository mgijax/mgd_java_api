package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;

@RequestScoped
public class MarkerDAO extends PostgresSQLDAO<ProbeMarker> {

	protected MarkerDAO() {
		super(ProbeMarker.class);
	}


}
