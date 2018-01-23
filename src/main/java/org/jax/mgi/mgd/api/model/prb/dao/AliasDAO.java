package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Alias;

@RequestScoped
public class AliasDAO extends PostgresSQLDAO<Alias> {

	protected AliasDAO() {
		super(Alias.class);
	}


}
