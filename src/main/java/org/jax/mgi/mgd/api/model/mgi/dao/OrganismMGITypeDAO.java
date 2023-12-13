package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.OrganismMGIType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganismMGITypeDAO extends PostgresSQLDAO<OrganismMGIType> {
	protected OrganismMGITypeDAO() {
		super(OrganismMGIType.class);
	}
}
