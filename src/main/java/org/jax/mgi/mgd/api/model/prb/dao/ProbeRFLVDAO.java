package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeRFLV;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeRFLVDAO extends PostgresSQLDAO<ProbeRFLV> {
	protected ProbeRFLVDAO() {
		super(ProbeRFLV.class);
	}
}
