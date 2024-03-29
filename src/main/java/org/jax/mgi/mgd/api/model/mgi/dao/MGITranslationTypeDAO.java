package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGITranslationType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGITranslationTypeDAO extends PostgresSQLDAO<MGITranslationType> {
	public MGITranslationTypeDAO() {
		super(MGITranslationType.class);
	}
}
