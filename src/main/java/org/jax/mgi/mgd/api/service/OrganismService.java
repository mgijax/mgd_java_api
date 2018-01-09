package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;

@RequestScoped
public class OrganismService extends ServiceInterface<Organism> {

	@Inject
	private OrganismDAO organismDAO;
	
	@Override
	public PostgresSQLDAO<Organism> getDAO() {
		return organismDAO;
	}

}
