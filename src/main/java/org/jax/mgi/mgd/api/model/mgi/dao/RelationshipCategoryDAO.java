package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipCategory;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationshipCategoryDAO extends PostgresSQLDAO<RelationshipCategory> {
	public RelationshipCategoryDAO() {
		super(RelationshipCategory.class);
	}
}
