package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

public class ReferenceDAO extends PostgresSQLDAO<Reference> {
	protected ReferenceDAO() {
		super(Reference.class);
	}
}
