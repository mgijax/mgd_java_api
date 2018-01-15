package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.ri.entities.RI_RISet;

@RequestScoped
public class RI_RISetDAO extends PostgresSQLDAO<RI_RISet> {

	protected RI_RISetDAO() {
		super(RI_RISet.class);
	}


}
