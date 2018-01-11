package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyType;

@RequestScoped
public class AntibodyTypeDAO extends PostgresSQLDAO<AntibodyType> {

	protected AntibodyTypeDAO() {
		super(AntibodyType.class);
	}


}
