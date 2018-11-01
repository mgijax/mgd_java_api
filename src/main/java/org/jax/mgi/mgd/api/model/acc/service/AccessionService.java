package org.jax.mgi.mgd.api.model.acc.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.acc.dao.AccessionDAO;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.search.AccessionSearchForm;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class AccessionService extends BaseService<AccessionDomain> implements BaseSearchInterface<AccessionDomain, AccessionSearchForm> {

	@Inject
	private AccessionDAO accessionDAO;

	private AccessionTranslator translator = new AccessionTranslator();
	
	@Transactional
	public SearchResults<AccessionDomain> create(AccessionDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AccessionDomain> update(AccessionDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AccessionDomain get(Integer key) {
		return translator.translate(accessionDAO.get(key),3);
	}

	@Transactional
	public SearchResults<AccessionDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AccessionDomain> search(AccessionSearchForm searchForm) {
		SearchResults<Accession> accessions;
		if(searchForm.getOrderBy() != null) {
			accessions = accessionDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			accessions = accessionDAO.search(searchForm.getSearchFields());
		}
		Iterable<AccessionDomain> newItems = translator.translateEntities(accessions.items, searchForm.getSearchDepth());
		return new SearchResults<AccessionDomain>(newItems);
	}



}
