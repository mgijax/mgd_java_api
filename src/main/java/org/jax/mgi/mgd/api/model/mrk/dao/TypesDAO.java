package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Type;

@RequestScoped
public class TypesDAO extends PostgresSQLDAO<Type> {

	protected TypesDAO() {
		super(Type.class);
	}


}
