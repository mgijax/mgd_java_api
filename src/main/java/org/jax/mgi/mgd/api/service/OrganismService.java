package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.dao.OrganismDAO;
import org.jax.mgi.mgd.api.entities.Organism;

@RequestScoped
public class OrganismService extends ServiceInterface<Organism> {

	@Inject
	private OrganismDAO organismDAO;
	
	@Override
	public PostgresSQLDAO<Organism> getDAO() {
		return organismDAO;
	}

}
