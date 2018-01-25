package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonymType;

@RequestScoped
public class MGISynonymTypeDAO extends PostgresSQLDAO<MGISynonymType> {

	public MGISynonymTypeDAO() {
		super(MGISynonymType.class);
	}

}
