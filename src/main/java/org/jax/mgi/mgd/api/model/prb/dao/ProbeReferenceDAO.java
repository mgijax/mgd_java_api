package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReference;

@RequestScoped
public class ProbeReferenceDAO extends PostgresSQLDAO<ProbeReference> {

	protected ProbeReferenceDAO() {
		super(ProbeReference.class);
	}


}
