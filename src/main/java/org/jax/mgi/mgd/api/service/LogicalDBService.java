package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.model.acc.dao.LogicalDBDAO;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class LogicalDBService extends ServiceInterface<LogicalDBDomain> {

	@Inject
	private LogicalDBDAO logicaldbDAO;

	@Override
	public LogicalDBDomain create(LogicalDBDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalDBDomain update(LogicalDBDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalDBDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalDBDomain delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<LogicalDBDomain> search(Map<String, Object> searchFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<LogicalDBDomain> search(Map<String, Object> searchFields, String orderByField) {
		// TODO Auto-generated method stub
		return null;
	}


}
