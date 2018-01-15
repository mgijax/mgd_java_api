package org.jax.mgi.mgd.api.model.ri.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.ri.entities.RISet;


@RequestScoped
public class RISetDAO extends PostgresSQLDAO<RISetDAO> {

	protected RISetDAO() {
		super(RISetDAO.class);
	}


}
