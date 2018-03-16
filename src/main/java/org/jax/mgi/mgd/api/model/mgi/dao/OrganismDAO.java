package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;

public class OrganismDAO extends PostgresSQLDAO<Organism> {
	protected OrganismDAO() {
		super(Organism.class);
	}
}
