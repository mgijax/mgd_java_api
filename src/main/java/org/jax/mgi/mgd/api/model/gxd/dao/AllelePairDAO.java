package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AllelePairDAO extends PostgresSQLDAO<AllelePair> {
	protected AllelePairDAO() {
		super(AllelePair.class);
	}
}
