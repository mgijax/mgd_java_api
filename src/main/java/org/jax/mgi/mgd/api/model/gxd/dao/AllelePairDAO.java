package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;

@RequestScoped
public class AllelePairDAO extends PostgresSQLDAO<AllelePair> {

	protected AllelePairDAO() {
		super(AllelePair.class);
	}


}
