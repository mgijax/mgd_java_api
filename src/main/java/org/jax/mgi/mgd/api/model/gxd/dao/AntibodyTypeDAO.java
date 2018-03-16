package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyType;

public class AntibodyTypeDAO extends PostgresSQLDAO<AntibodyType> {
	protected AntibodyTypeDAO() {
		super(AntibodyType.class);
	}
}
