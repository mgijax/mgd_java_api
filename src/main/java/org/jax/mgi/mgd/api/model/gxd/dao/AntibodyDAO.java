package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AntibodyDAO extends PostgresSQLDAO<Antibody> {
	protected AntibodyDAO() {
		super(Antibody.class);
	}
}
