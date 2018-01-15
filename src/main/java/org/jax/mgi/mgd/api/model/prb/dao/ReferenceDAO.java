package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Reference;

@RequestScoped
public class ReferenceDAO extends PostgresSQLDAO<Reference> {

	protected ReferenceDAO() {
		super(Reference.class);
	}


}
