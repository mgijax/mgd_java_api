package org.jax.mgi.mgd.api.model.acc.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.acc.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.acc.search.MGITypeSearchForm;
import org.jax.mgi.mgd.api.model.acc.translator.MGITypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MGITypeService extends BaseService<MGITypeDomain> implements BaseSearchInterface<MGITypeDomain, MGITypeSearchForm> {
	
	private MGITypeTranslator translator = new MGITypeTranslator();
	
	@Inject
	private MGITypeDAO mgitypeDAO;
	

	@Transactional
	public MGITypeDomain create(MGITypeDomain object, User user) throws APIException {
		MGIType type = translator.translate(object);
		MGIType returnType = mgitypeDAO.create(type);
		MGITypeDomain typeDomain = translator.translate(returnType);
		return typeDomain;
	}

	@Transactional
	public MGITypeDomain update(MGITypeDomain object, User user) {
		MGIType type = translator.translate(object);
		MGIType returnType = mgitypeDAO.update(type);
		MGITypeDomain typeDomain = translator.translate(returnType);
		return typeDomain;
	}

	@Transactional
	public MGITypeDomain get(Integer key) {
		return translator.translate(mgitypeDAO.get(key));
	}

	@Transactional
	public MGITypeDomain delete(Integer key, User user) {
		return translator.translate(mgitypeDAO.delete(mgitypeDAO.get(key)));
	}

	@Transactional
	public SearchResults<MGITypeDomain> search(MGITypeSearchForm searchForm) {
		SearchResults<MGIType> mgiTypes;
		if(searchForm.getOrderBy() != null) {
			mgiTypes = mgitypeDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			mgiTypes = mgitypeDAO.search(searchForm.getSearchFields());
		}
		Iterable<MGITypeDomain> newItems = translator.translateEntities(mgiTypes.items);
		return new SearchResults<MGITypeDomain>(newItems);
	}

}
