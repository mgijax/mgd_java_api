package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Tissue;

@RequestScoped
public class TissueDAO extends PostgresSQLDAO<Tissue> {

	protected TissueDAO() {
		super(Tissue.class);
	}


}
