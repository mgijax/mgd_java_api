package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RefAssocType;

public class RefAssocTypeDAO extends PostgresSQLDAO<RefAssocType> {
	public RefAssocTypeDAO() {
		super(RefAssocType.class);
	}
}
