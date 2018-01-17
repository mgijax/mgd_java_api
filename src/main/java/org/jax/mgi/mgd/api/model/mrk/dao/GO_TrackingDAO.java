package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.GO_Tracking;

@RequestScoped
public class GO_TrackingDAO extends PostgresSQLDAO<GO_Tracking> {

	protected GO_TrackingDAO() {
		super(GO_Tracking.class);
	}


}
