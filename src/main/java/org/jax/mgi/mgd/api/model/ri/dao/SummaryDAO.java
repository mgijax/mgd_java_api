package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;

@RequestScoped
public class SummaryDAO extends PostgresSQLDAO<SummaryDAO> {

	protected SummaryDAO() {
		super(SummaryDAO.class);
	}


}
