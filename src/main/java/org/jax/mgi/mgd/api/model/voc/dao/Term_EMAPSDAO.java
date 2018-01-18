package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term_EMAPS;

@RequestScoped
public class Term_EMAPSDAO extends PostgresSQLDAO<Term_EMAPS> {

	public Term_EMAPSDAO() {
		super(Term_EMAPS.class);
	}

}
