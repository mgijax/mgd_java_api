package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGITranslation;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGITranslationDAO extends PostgresSQLDAO<MGITranslation> {
	public MGITranslationDAO() {
		super(MGITranslation.class);
	}
}
