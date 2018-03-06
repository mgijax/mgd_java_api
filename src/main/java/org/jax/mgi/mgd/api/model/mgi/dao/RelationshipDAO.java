package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;

public class RelationshipDAO extends PostgresSQLDAO<Relationship> {
	public RelationshipDAO() {
		super(Relationship.class);
	}
}
