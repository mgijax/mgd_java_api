package org.jax.mgi.mgd.api.model.acc.service;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.model.acc.search.ActualDBSearchForm;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ActualDBService extends BaseService<ActualDBDomain> implements BaseSearchInterface<ActualDBDomain, ActualDBSearchForm>{

//	@Inject
//	private ActualDBDAO actualdbDAO;

	@Override
	public ActualDBDomain create(ActualDBDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActualDBDomain update(ActualDBDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActualDBDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<ActualDBDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<ActualDBDomain> search(ActualDBSearchForm searchForm) {
		// TODO Auto-generated method stub
		return null;
	}


}
