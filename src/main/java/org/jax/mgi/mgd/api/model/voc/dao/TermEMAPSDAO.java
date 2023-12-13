package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPS;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TermEMAPSDAO extends PostgresSQLDAO<TermEMAPS> {
	public TermEMAPSDAO() {
		super(TermEMAPS.class);
	}
}
