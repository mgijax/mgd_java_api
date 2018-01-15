package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term_EMAPA;

@RequestScoped
public class Term_EMAPADAO extends PostgresSQLDAO<Term_EMAPA> {

	public Term_EMAPADAO() {
		super(Term_EMAPA.class);
	}

}
