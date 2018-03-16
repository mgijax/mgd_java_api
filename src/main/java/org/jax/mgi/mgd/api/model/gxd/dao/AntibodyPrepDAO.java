package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyPrep;

public class AntibodyPrepDAO extends PostgresSQLDAO<AntibodyPrep> {
	protected AntibodyPrepDAO() {
		super(AntibodyPrep.class);
	}
}
