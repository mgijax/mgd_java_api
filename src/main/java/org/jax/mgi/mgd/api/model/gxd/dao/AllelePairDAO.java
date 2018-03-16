package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;

public class AllelePairDAO extends PostgresSQLDAO<AllelePair> {
	protected AllelePairDAO() {
		super(AllelePair.class);
	}
}
