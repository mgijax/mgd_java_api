package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlleleVariantDAO extends PostgresSQLDAO<AlleleVariant> {
	protected AlleleVariantDAO() {
		super(AlleleVariant.class);
	}
}
