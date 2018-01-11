package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelControl;

@RequestScoped
public class GelControlDAO extends PostgresSQLDAO<GelControl> {

	protected GelControlDAO() {
		super(GelControl.class);
	}


}
