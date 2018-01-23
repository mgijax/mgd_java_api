package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

@RequestScoped
public class TermService extends ServiceInterface<Term> {

	@Inject
	private TermDAO termDAO;
	
	@Override
	public PostgresSQLDAO<Term> getDAO() {
		return termDAO;
	}

}
