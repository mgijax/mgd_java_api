package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelUnit;

@RequestScoped
public class GelUnitDAO extends PostgresSQLDAO<GelUnit> {

	protected GelUnitDAO() {
		super(GelUnit.class);
	}


}
