package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIRefAssocType;

public class MGIRefAssocTypeDAO extends PostgresSQLDAO<MGIRefAssocType> {
	public MGIRefAssocTypeDAO() {
		super(MGIRefAssocType.class);
	}
}
