package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.ProbeSense;

public class ProbeSenseDAO extends PostgresSQLDAO<ProbeSense> {
	protected ProbeSenseDAO() {
		super(ProbeSense.class);
	}
}
