package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeRFLV;

public class ProbeRFLVDAO extends PostgresSQLDAO<ProbeRFLV> {
	protected ProbeRFLVDAO() {
		super(ProbeRFLV.class);
	}
}
