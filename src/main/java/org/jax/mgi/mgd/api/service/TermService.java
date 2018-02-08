package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.translators.TermTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class TermService extends ServiceInterface<TermDomain> {

	@Inject
	private TermDAO termDAO;
	private TermTranslator translator = new TermTranslator();
	
	public TermDomain create(TermDomain object) {
		Term term = translator.translate(object);
		Term returnTerm = termDAO.create(term);
		TermDomain termDomain = translator.translate(returnTerm);
		return termDomain;
	}

	public TermDomain update(TermDomain object) {
		Term term = translator.translate(object);
		Term returnTerm = termDAO.update(term);
		TermDomain termDomain = translator.translate(returnTerm);
		return termDomain;
	}

	public TermDomain get(Integer key) {
		return translator.translate(termDAO.get(key));
	}
	
	public TermDomain delete(Integer key) {
		return translator.translate(termDAO.delete(termDAO.get(key)));
	}

	public SearchResults<TermDomain> search(Map<String, Object> searchFields) {
		SearchResults<Term> terms = termDAO.search(searchFields);
		Iterable<TermDomain> newItems = translator.translateEntities(terms.items);
		return new SearchResults<TermDomain>(newItems);
	}
	
	public SearchResults<TermDomain> search(Map<String, Object> searchFields, String orderByField) {
		SearchResults<Term> terms = termDAO.search(searchFields, orderByField);
		Iterable<TermDomain> newItems = translator.translateEntities(terms.items);
		return new SearchResults<TermDomain>(newItems);
	}
	

}
