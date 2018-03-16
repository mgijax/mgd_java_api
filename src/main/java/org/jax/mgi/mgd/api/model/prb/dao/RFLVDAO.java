package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.RFLV;

public class RFLVDAO extends PostgresSQLDAO<RFLV> {
	protected RFLVDAO() {
		super(RFLV.class);
	}
}
