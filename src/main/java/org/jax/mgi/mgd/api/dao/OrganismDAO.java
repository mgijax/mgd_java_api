package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Organism;

@RequestScoped
public class OrganismDAO extends PostgresSQLDAO<Organism> {

	protected OrganismDAO() {
		super(Organism.class);
	}

}
