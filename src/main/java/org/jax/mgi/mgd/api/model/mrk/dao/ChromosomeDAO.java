package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Chromosome;

@RequestScoped
public class ChromosomeDAO extends PostgresSQLDAO<Chromosome> {

	protected ChromosomeDAO() {
		super(Chromosome.class);
	}


}
