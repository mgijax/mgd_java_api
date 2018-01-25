package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;

@RequestScoped
public class ExptMarkerDAO extends PostgresSQLDAO<ExptMarker> {

	protected ExptMarkerDAO() {
		super(ExptMarker.class);
	}


}
