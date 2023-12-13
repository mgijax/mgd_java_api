package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Chromosome;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChromosomeDAO extends PostgresSQLDAO<Chromosome> {
	protected ChromosomeDAO() {
		super(Chromosome.class);
	}
}
