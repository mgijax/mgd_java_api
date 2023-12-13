package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGISynonymDAO extends PostgresSQLDAO<MGISynonym> {
	public MGISynonymDAO() {
		super(MGISynonym.class);
	}
}
