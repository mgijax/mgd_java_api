package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyClass;

@RequestScoped
public class AntibodyClassDAO extends PostgresSQLDAO<AntibodyClass> {

	protected AntibodyClassDAO() {
		super(AntibodyClass.class);
	}


}
