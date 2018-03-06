package org.jax.mgi.mgd.api.model.mgi.service;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.OrganismSearchForm;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class OrganismService extends BaseService<OrganismDomain> implements BaseSearchInterface<OrganismDomain, OrganismSearchForm> {

//	@Inject
//	private OrganismDAO organismDAO;

	@Override
	public OrganismDomain create(OrganismDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain update(OrganismDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<OrganismDomain> search(OrganismSearchForm searchForm) {
		// TODO Auto-generated method stub
		return null;
	}



}
