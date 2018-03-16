package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.ProbePrep;

public class ProbePrepDAO extends PostgresSQLDAO<ProbePrep> {
	protected ProbePrepDAO() {
		super(ProbePrep.class);
	}
}
