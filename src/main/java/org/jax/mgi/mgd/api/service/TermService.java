package org.jax.mgi.mgd.api.service;

import java.util.HashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.TermDAO;
import org.jax.mgi.mgd.api.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.entities.Vocabulary;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class TermService {

	@Inject
	private TermDAO termDAO;
	
	@Inject
	VocabularyDAO vocabDAO;
	
	public Term createTerm(Term term) {
		return termDAO.add(term);
	}

	public Term updateTerm(Term term) {
		return termDAO.update(term);
	}

	public SearchResults<Term> getTerm(HashMap<String, Object> searchFields) {
		return termDAO.search(searchFields);
	}
	
	public SearchResults<Term> searchTerm(HashMap<String, Object> searchFields) {
		return termDAO.search(searchFields);
	}
	
	public SearchResults<Vocabulary> searchVocab(HashMap<String, Object> searchFields) {
		return vocabDAO.search(searchFields);
	}
	
	public SearchResults<Term> deleteTerm(String term_key) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(term_key != null) { map.put("_term_key", term_key); }
		return termDAO.delete(termDAO.get(map));
	}
}
