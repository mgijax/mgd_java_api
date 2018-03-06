package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAllele;

public class ProbeAlleleDAO extends PostgresSQLDAO<ProbeAllele> {
	protected ProbeAlleleDAO() {
		super(ProbeAllele.class);
	}
}
