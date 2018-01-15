package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

@RequestScoped
public class ProbeDAO extends PostgresSQLDAO<Probe> {

	protected ProbeDAO() {
		super(Probe.class);
	}


}
