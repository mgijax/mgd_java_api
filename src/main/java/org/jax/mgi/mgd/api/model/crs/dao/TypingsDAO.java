package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossTyping;

@RequestScoped
public class TypingsDAO extends PostgresSQLDAO<CrossTyping> {

	public TypingsDAO() {
		super(CrossTyping.class);
	}

}
