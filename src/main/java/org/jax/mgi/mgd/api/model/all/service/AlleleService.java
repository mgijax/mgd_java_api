package org.jax.mgi.mgd.api.model.all.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.search.AlleleSearchForm;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class AlleleService extends BaseService<AlleleDomain> implements BaseSearchInterface<AlleleDomain, AlleleSearchForm> {

	@Inject
	private AlleleDAO alleleDAO;

	private AlleleTranslator translator = new AlleleTranslator();
	
	@Transactional
	public AlleleDomain create(AlleleDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AlleleDomain update(AlleleDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AlleleDomain get(Integer key) {
		return translator.translate(alleleDAO.get(key));
	}

	@Transactional
	public AlleleDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AlleleDomain> search(AlleleSearchForm searchForm) {
		SearchResults<Allele> alleles;
		if(searchForm.getOrderBy() != null) {
			alleles = alleleDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			alleles = alleleDAO.search(searchForm.getSearchFields());
		}
		Iterable<AlleleDomain> newItems = translator.translateEntities(alleles.items, searchForm.getSearchDepth());
		return new SearchResults<AlleleDomain>(newItems);
	}



}
