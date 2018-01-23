package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

@RequestScoped
public class TermDAO extends PostgresSQLDAO<Term> {

	protected TermDAO() {
		super(Term.class);
	}

}
