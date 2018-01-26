package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDLabel;

@RequestScoped
public class GXDLabelDAO extends PostgresSQLDAO<GXDLabel> {

	protected GXDLabelDAO() {
		super(GXDLabel.class);
	}


}
