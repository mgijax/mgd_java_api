package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.Progeny;

@RequestScoped
public class ProgenyDAO extends PostgresSQLDAO<Progeny> {

	public ProgenyDAO() {
		super(Progeny.class);
	}

}
