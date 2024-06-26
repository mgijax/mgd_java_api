package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceBook;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReferenceBookDAO extends PostgresSQLDAO<ReferenceBook> {
	protected ReferenceBookDAO() {
		super(ReferenceBook.class);
	}
}
