package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelUnits;

@RequestScoped
public class GelUnitsDAO extends PostgresSQLDAO<GelUnits> {

	protected GelUnitsDAO() {
		super(GelUnits.class);
	}


}
