package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyPrep;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AntibodyPrepDAO extends PostgresSQLDAO<AntibodyPrep> {
	protected AntibodyPrepDAO() {
		super(AntibodyPrep.class);
	}
}
