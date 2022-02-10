package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipFEAR;

public class RelationshipFEARDAO extends PostgresSQLDAO<RelationshipFEAR> {
	public RelationshipFEARDAO() {
		super(RelationshipFEAR.class);
	}
}
