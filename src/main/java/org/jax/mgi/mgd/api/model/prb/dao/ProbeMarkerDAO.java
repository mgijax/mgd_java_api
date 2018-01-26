package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;

@RequestScoped
public class ProbeMarkerDAO extends PostgresSQLDAO<ProbeMarker> {

	protected ProbeMarkerDAO() {
		super(ProbeMarker.class);
	}


}
