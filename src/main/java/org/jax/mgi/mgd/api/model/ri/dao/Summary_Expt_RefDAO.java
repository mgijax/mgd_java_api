package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;

@RequestScoped
public class Summary_Expt_RefDAO extends PostgresSQLDAO<Summary_Expt_RefDAO> {

	protected Summary_Expt_RefDAO() {
		super(Summary_Expt_RefDAO.class);
	}


}
