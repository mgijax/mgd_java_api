package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.GOTracking;

@RequestScoped
public class GOTrackingDAO extends PostgresSQLDAO<GOTracking> {

	protected GOTrackingDAO() {
		super(GOTracking.class);
	}


}
