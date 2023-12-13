package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Antigen;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AntigenDAO extends PostgresSQLDAO<Antigen> {
	protected AntigenDAO() {
		super(Antigen.class);
	}
}
