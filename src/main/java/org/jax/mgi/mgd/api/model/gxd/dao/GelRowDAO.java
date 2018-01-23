package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.GelRow;

@RequestScoped
public class GelRowDAO extends PostgresSQLDAO<GelRow> {

	protected GelRowDAO() {
		super(GelRow.class);
	}


}
