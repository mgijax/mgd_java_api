package org.jax.mgi.mgd.api.dao;

import org.jax.mgi.mgd.api.entities.Term;

public class MetadataDAO extends PostgresSQLDAO<Term> {

	protected MetadataDAO() {
		super(Term.class);
	}

}
