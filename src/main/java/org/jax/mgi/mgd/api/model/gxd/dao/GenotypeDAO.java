package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GenotypeDAO extends PostgresSQLDAO<Genotype> {
	protected GenotypeDAO() {
		super(Genotype.class);
	}
}
