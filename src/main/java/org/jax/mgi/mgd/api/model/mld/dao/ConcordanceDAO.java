package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Concordance;

public class ConcordanceDAO extends PostgresSQLDAO<Concordance> {
	protected ConcordanceDAO() {
		super(Concordance.class);
	}
}
