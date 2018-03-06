package org.jax.mgi.mgd.api.model.acc.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.acc.dao.LogicalDBDAO;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.model.acc.search.LogicalDBSearchForm;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.service.ServiceInterface;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class LogicalDBService extends ServiceInterface<LogicalDBDomain, LogicalDBSearchForm> {

	@Inject
	private LogicalDBDAO logicaldbDAO;

	@Override
	public LogicalDBDomain create(LogicalDBDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalDBDomain update(LogicalDBDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalDBDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalDBDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<LogicalDBDomain> search(LogicalDBSearchForm searchForm) {
		// TODO Auto-generated method stub
		return null;
	}




}
