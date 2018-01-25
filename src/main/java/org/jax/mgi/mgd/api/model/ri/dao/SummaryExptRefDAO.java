package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;

@RequestScoped
public class SummaryExptRefDAO extends PostgresSQLDAO<SummaryExptRefDAO> {

	protected SummaryExptRefDAO() {
		super(SummaryExptRefDAO.class);
	}


}
