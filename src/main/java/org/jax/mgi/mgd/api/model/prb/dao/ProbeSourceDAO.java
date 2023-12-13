package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeSourceDAO extends PostgresSQLDAO<ProbeSource> {
	protected ProbeSourceDAO() {
		super(ProbeSource.class);
	}
}
