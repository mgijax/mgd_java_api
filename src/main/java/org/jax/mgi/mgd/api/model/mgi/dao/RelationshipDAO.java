package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;

@RequestScoped
public class RelationshipDAO extends PostgresSQLDAO<Relationship> {

	public RelationshipDAO() {
		super(Relationship.class);
	}

}
