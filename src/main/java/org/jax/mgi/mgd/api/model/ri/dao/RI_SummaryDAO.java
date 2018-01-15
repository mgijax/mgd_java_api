package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.ri.entities.RI_Summary;

@RequestScoped
public class RI_SummaryDAO extends PostgresSQLDAO<RI_Summary> {

	protected RI_SummaryDAO() {
		super(RI_Summary.class);
	}


}
