package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.TermDAO;
import org.jax.mgi.mgd.api.entities.Term;

@RequestScoped
public class TermService {

	@Inject
	private TermDAO termDAO;
	
	public Term createTerm(Term term) {
		return termDAO.add(term);
	}

	public Term updateTerm(Term term) {
		return termDAO.update(term);
	}
	
	public List<Term> getTerm(HashMap<String, Object> searchFields) {
		return termDAO.get(searchFields);
	}
	
	public Term deleteTerm(String term_key) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(term_key != null) { map.put("_term_key", term_key); }
		Term term = termDAO.get(map).get(0);
		return termDAO.delete(term);
	}
}
