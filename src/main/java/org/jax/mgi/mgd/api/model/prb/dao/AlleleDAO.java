package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Allele;

@RequestScoped
public class AlleleDAO extends PostgresSQLDAO<Allele> {

	protected AlleleDAO() {
		super(Allele.class);
	}


}
