package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Set;

@RequestScoped
public class SetDAO extends PostgresSQLDAO<Set> {

	public SetDAO() {
		super(Set.class);
	}

}
