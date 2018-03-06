package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyAlias;

public class AntibodyAliasDAO extends PostgresSQLDAO<AntibodyAlias> {
	protected AntibodyAliasDAO() {
		super(AntibodyAlias.class);
	}
}
