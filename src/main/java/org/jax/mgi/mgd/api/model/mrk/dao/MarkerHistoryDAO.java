package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;

public class MarkerHistoryDAO extends PostgresSQLDAO<MarkerHistory> {
	protected MarkerHistoryDAO() {
		super(MarkerHistory.class);
	}
	
}
