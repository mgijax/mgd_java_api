package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyClass;

public class AntibodyClassDAO extends PostgresSQLDAO<AntibodyClass> {
	protected AntibodyClassDAO() {
		super(AntibodyClass.class);
	}
}
