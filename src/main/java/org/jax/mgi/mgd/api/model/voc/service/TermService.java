package org.jax.mgi.mgd.api.model.voc.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.voc.search.TermSearchForm;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class TermService implements BaseSearchInterface<TermDomain, TermSearchForm> {

	@Inject
	private TermDAO termDAO;
	
	private TermTranslator translator = new TermTranslator();

	@Override
	public SearchResults<TermDomain> search(TermSearchForm searchForm) {
		SearchResults<Term> terms;
		if(searchForm.getOrderBy() != null) {
			terms = termDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			terms = termDAO.search(searchForm.getSearchFields());
		}
		Iterable<TermDomain> newItems = translator.translateEntities(terms.items);
		return new SearchResults<TermDomain>(newItems);
	}

}