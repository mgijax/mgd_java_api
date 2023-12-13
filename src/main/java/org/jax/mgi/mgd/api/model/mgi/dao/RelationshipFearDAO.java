package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipFear;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationshipFearDAO extends PostgresSQLDAO<RelationshipFear> {
	public RelationshipFearDAO() {
		super(RelationshipFear.class);
	}
}
