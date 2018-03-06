package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Chromosome;

public class ChromosomeDAO extends PostgresSQLDAO<Chromosome> {
	protected ChromosomeDAO() {
		super(Chromosome.class);
	}
}
