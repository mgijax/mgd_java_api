package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;

@RequestScoped
public class RISummaryDAO extends PostgresSQLDAO<RISummaryDAO> {

	protected RISummaryDAO() {
		super(RISummaryDAO.class);
	}


}
