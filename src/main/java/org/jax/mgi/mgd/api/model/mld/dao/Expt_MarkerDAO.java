package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Expt_Marker;

@RequestScoped
public class Expt_MarkerDAO extends PostgresSQLDAO<Expt_Marker> {

	protected Expt_MarkerDAO() {
		super(Expt_Marker.class);
	}


}
