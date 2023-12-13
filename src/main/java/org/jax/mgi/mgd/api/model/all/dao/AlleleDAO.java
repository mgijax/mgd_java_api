package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlleleDAO extends PostgresSQLDAO<Allele> {
	protected AlleleDAO() {
		super(Allele.class);
	}
}
