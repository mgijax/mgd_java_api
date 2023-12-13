package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MGIReferenceAssocDAO extends PostgresSQLDAO<MGIReferenceAssoc> {
	public MGIReferenceAssocDAO() {
		super(MGIReferenceAssoc.class);
	}
}
