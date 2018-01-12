package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship_Property;

@RequestScoped
public class Relationship_PropertyDAO extends PostgresSQLDAO<Relationship_Property> {

	public Relationship_PropertyDAO() {
		super(Relationship_Property.class);
	}

}
