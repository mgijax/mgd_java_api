package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.Typings;

@RequestScoped
public class TypingsDAO extends PostgresSQLDAO<Typings> {

	public TypingsDAO() {
		super(Typings.class);
	}

}
