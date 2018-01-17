package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.GO_Tracking;

@RequestScoped
public class GOTrackingDAO extends PostgresSQLDAO<GO_Tracking> {

	protected GOTrackingDAO() {
		super(GO_Tracking.class);
	}


}
