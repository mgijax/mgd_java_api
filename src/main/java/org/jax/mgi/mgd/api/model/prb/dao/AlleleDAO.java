package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAllele;

@RequestScoped
public class AlleleDAO extends PostgresSQLDAO<ProbeAllele> {

	protected AlleleDAO() {
		super(ProbeAllele.class);
	}


}
