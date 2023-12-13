package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MetadataDAO extends PostgresSQLDAO<Term> {
	protected MetadataDAO() {
		super(Term.class);
	}
}
