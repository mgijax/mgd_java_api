package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Reference;

@RequestScoped
public class ReferenceDAO extends PostgresSQLDAO<Reference> {

	public ReferenceDAO() {
		clazz = Reference.class;
	}
}
