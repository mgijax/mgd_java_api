package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.FixationMethod;

@RequestScoped
public class FixationMethodDAO extends PostgresSQLDAO<FixationMethod> {

	protected FixationMethodDAO() {
		super(FixationMethod.class);
	}


}
