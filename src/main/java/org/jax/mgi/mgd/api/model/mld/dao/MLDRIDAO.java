package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDRI;

public class MLDRIDAO extends PostgresSQLDAO<MLDRI> {
	protected MLDRIDAO() {
		super(MLDRI.class);
	}
}
