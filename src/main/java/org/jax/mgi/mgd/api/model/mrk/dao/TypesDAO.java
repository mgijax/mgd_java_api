package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Types;

@RequestScoped
public class TypesDAO extends PostgresSQLDAO<Types> {

	protected TypesDAO() {
		super(Types.class);
	}


}
