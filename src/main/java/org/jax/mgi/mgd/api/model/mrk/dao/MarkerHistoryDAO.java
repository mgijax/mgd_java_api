package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;

@RequestScoped
public class MarkerHistoryDAO extends PostgresSQLDAO<MarkerHistory> {

	protected MarkerHistoryDAO() {
		super(MarkerHistory.class);
	}


}
