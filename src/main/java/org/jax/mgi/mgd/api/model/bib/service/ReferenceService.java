package org.jax.mgi.mgd.api.model.bib.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.search.ReferenceSearchForm;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ReferenceService extends BaseService<ReferenceDomain> implements BaseSearchInterface<ReferenceDomain, ReferenceSearchForm> {

	@Inject
	private ReferenceDAO referenceDAO;

	private ReferenceTranslator translator = new ReferenceTranslator();
	
	@Transactional
	public ReferenceDomain create(ReferenceDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ReferenceDomain update(ReferenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ReferenceDomain get(Integer key) {
		return translator.translate(referenceDAO.get(key));
	}

	@Transactional
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ReferenceDomain> search(ReferenceSearchForm searchForm) {
		SearchResults<Reference> references;
		if(searchForm.getOrderBy() != null) {
			references = referenceDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			references = referenceDAO.search(searchForm.getSearchFields());
		}
		Iterable<ReferenceDomain> newItems = translator.translateEntities(references.items, searchForm.getSearchDepth());
		return new SearchResults<ReferenceDomain>(newItems);
	}
}
