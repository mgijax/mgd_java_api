package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReference;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeReferenceDAO extends PostgresSQLDAO<ProbeReference> {
	protected ProbeReferenceDAO() {
		super(ProbeReference.class);
	}
}
