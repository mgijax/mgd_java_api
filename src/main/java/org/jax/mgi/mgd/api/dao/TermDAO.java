package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Term;

@RequestScoped
public class TermDAO extends PostgresSQLDAO<Term> {

	protected TermDAO() {
		super(Term.class);
	}

}
