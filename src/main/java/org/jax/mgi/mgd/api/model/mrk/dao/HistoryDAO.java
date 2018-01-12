package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.History;

@RequestScoped
public class HistoryDAO extends PostgresSQLDAO<History> {

	protected HistoryDAO() {
		super(History.class);
	}


}
