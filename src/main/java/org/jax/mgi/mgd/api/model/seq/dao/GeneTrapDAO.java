package org.jax.mgi.mgd.api.model.seq.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.GeneTrap;

public class GeneTrapDAO extends PostgresSQLDAO<GeneTrap> {
	protected GeneTrapDAO() {
		super(GeneTrap.class);
	}
}
