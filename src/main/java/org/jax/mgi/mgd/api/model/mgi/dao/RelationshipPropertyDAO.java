package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationshipPropertyDAO extends PostgresSQLDAO<RelationshipProperty> {
	public RelationshipPropertyDAO() {
		super(RelationshipProperty.class);
	}
}
