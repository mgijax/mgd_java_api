package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.SynonymType;

@RequestScoped
public class SynonymTypeDAO extends PostgresSQLDAO<SynonymType> {

	public SynonymTypeDAO() {
		super(SynonymType.class);
	}

}
