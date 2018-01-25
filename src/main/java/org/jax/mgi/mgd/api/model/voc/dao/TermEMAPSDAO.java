package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPS;

@RequestScoped
public class TermEMAPSDAO extends PostgresSQLDAO<TermEMAPS> {

	public TermEMAPSDAO() {
		super(TermEMAPS.class);
	}

}
