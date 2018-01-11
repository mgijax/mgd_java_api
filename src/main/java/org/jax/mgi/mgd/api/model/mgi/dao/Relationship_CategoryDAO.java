package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship_Category;

@RequestScoped
public class Relationship_CategoryDAO extends PostgresSQLDAO<Relationship_Category> {

	public Relationship_CategoryDAO() {
		super(Relationship_Category.class);
	}

}
