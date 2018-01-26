package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAlias;

@RequestScoped
public class ProbeAliasDAO extends PostgresSQLDAO<ProbeAlias> {

	protected ProbeAliasDAO() {
		super(ProbeAlias.class);
	}


}
