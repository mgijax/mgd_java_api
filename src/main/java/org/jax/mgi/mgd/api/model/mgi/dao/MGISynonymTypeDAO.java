package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonymType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGISynonymTypeDAO extends PostgresSQLDAO<MGISynonymType> {
	public MGISynonymTypeDAO() {
		super(MGISynonymType.class);
	}
}
