package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPS;

public class TermEMAPSDAO extends PostgresSQLDAO<TermEMAPS> {
	public TermEMAPSDAO() {
		super(TermEMAPS.class);
	}
}
