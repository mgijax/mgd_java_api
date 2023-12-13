package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeDAO extends PostgresSQLDAO<Probe> {
	protected ProbeDAO() {
		super(Probe.class);
	}
}
