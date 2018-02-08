package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MGITypeDomain;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MGITypeService extends ServiceInterface<MGITypeDomain> {
	
	@Inject
	private MGITypeDAO mgitypeDAO;

	@Override
	public MGITypeDomain create(MGITypeDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MGITypeDomain update(MGITypeDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MGITypeDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MGITypeDomain delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<MGITypeDomain> search(Map<String, Object> searchFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<MGITypeDomain> search(Map<String, Object> searchFields, String orderByField) {
		// TODO Auto-generated method stub
		return null;
	}

}
