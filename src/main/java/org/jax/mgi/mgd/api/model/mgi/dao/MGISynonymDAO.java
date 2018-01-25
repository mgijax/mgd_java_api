package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;

@RequestScoped
public class MGISynonymDAO extends PostgresSQLDAO<MGISynonym> {

	public MGISynonymDAO() {
		super(MGISynonym.class);
	}

}
