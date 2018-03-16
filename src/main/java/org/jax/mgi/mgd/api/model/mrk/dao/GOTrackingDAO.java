package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.GOTracking;

public class GOTrackingDAO extends PostgresSQLDAO<GOTracking> {
	protected GOTrackingDAO() {
		super(GOTracking.class);
	}
}
