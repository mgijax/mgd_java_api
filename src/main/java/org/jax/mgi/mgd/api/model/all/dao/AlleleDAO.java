package org.jax.mgi.mgd.api.model.all.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

@RequestScoped
public class AlleleDAO extends PostgresSQLDAO<Allele> {

	protected AlleleDAO() {
		super(Allele.class);
	}


}
