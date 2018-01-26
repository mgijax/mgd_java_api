package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;

@RequestScoped
public class ProbeSourceDAO extends PostgresSQLDAO<ProbeSource> {

	protected ProbeSourceDAO() {
		super(ProbeSource.class);
	}


}
