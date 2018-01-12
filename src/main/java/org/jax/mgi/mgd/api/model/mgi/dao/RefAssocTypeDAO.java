package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.RefAssocType;

@RequestScoped
public class RefAssocTypeDAO extends PostgresSQLDAO<RefAssocType> {

	public RefAssocTypeDAO() {
		super(RefAssocType.class);
	}

}
