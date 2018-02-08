package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.model.acc.dao.ActualDBDAO;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ActualDBService extends ServiceInterface<ActualDBDomain> {

	@Inject
	private ActualDBDAO actualdbDAO;

	@Override
	public ActualDBDomain create(ActualDBDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActualDBDomain update(ActualDBDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActualDBDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActualDBDomain delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<ActualDBDomain> search(Map<String, Object> searchFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<ActualDBDomain> search(Map<String, Object> searchFields, String orderByField) {
		// TODO Auto-generated method stub
		return null;
	}


}
