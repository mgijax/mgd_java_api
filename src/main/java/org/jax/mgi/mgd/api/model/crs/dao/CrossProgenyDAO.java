package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossProgeny;

@RequestScoped
public class CrossProgenyDAO extends PostgresSQLDAO<CrossProgeny> {

	public CrossProgenyDAO() {
		super(CrossProgeny.class);
	}

}
