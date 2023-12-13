package org.jax.mgi.mgd.api.model.acc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.AccessionReference;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessionReferenceDAO extends PostgresSQLDAO<AccessionReference> {
	protected AccessionReferenceDAO() {
		super(AccessionReference.class);
	}
}
