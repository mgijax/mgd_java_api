package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Secondary;

public class SecondaryDAO extends PostgresSQLDAO<Secondary> {
	protected SecondaryDAO() {
		super(Secondary.class);
	}
}
