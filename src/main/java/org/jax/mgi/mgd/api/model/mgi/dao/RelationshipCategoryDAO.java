package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipCategory;

@RequestScoped
public class RelationshipCategoryDAO extends PostgresSQLDAO<RelationshipCategory> {

	public RelationshipCategoryDAO() {
		super(RelationshipCategory.class);
	}

}
