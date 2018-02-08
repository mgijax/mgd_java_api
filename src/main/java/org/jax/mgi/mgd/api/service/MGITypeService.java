package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.translators.MGITypeTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MGITypeService extends ServiceInterface<MGITypeDomain> {
	
	private MGITypeTranslator translator = new MGITypeTranslator();
	
	@Inject
	private MGITypeDAO mgitypeDAO;
	

	@Override
	public MGITypeDomain create(MGITypeDomain object) {
		MGIType type = translator.translate(object);
		MGIType returnType = mgitypeDAO.create(type);
		MGITypeDomain typeDomain = translator.translate(returnType);
		return typeDomain;
	}

	@Override
	public MGITypeDomain update(MGITypeDomain object) {
		MGIType type = translator.translate(object);
		MGIType returnType = mgitypeDAO.update(type);
		MGITypeDomain typeDomain = translator.translate(returnType);
		return typeDomain;
	}

	@Override
	public MGITypeDomain get(Integer key) {
		return translator.translate(mgitypeDAO.get(key));
	}

	@Override
	public MGITypeDomain delete(Integer key) {
		return translator.translate(mgitypeDAO.delete(mgitypeDAO.get(key)));
	}

	@Override
	public SearchResults<MGITypeDomain> search(Map<String, Object> searchFields) {
		SearchResults<MGIType> types = mgitypeDAO.search(searchFields);
		Iterable<MGITypeDomain> newItems = translator.translateEntities(types.items);
		return new SearchResults<MGITypeDomain>(newItems);
	}

	@Override
	public SearchResults<MGITypeDomain> search(Map<String, Object> searchFields, String orderByField) {
		SearchResults<MGIType> types = mgitypeDAO.search(searchFields, orderByField);
		Iterable<MGITypeDomain> newItems = translator.translateEntities(types.items);
		return new SearchResults<MGITypeDomain>(newItems);
	}

}
