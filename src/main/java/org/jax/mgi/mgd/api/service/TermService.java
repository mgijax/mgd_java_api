package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.TermDAO;
import org.jax.mgi.mgd.api.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.entities.Vocabulary;
import org.jax.mgi.mgd.api.translators.TermTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class TermService {

	@Inject
	private TermDAO termDAO;

	@Inject
	VocabularyDAO vocabDAO;

	TermTranslator t = new TermTranslator();

	public Term createTerm(Term term) {
		return termDAO.add(term);
	}

	public Term updateTerm(Term term) {
		return termDAO.update(term);
	}

	public TermDomain getTerm(Integer primaryKey) {
		//Term term = termDAO.get("_term_key", primaryKey);
		//return t.translate(term);
		return null;
	}

	public SearchResults<TermDomain> searchTerm(HashMap<String, Object> searchFields) {

		SearchResults<Term> databaseResults = termDAO.search(searchFields);

		SearchResults<TermDomain> results = new SearchResults<>();
		List<TermDomain> domains = (List<TermDomain>) t.translateEntities(databaseResults.items);
		results.setItems(domains);
		return results;
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
