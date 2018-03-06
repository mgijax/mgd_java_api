package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAlias;

public class ProbeAliasDAO extends PostgresSQLDAO<ProbeAlias> {
	protected ProbeAliasDAO() {
		super(ProbeAlias.class);
	}
}
