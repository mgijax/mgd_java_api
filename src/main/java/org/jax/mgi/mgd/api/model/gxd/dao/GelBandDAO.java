package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelBand;

@RequestScoped
public class GelBandDAO extends PostgresSQLDAO<GelBand> {

	protected GelBandDAO() {
		super(GelBand.class);
	}


}
