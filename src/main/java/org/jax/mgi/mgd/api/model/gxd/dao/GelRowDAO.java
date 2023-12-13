package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelRow;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GelRowDAO extends PostgresSQLDAO<GelRow> {
	protected GelRowDAO() {
		super(GelRow.class);
	}
}
