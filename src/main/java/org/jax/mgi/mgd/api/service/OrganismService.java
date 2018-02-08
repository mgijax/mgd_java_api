package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class OrganismService extends ServiceInterface<OrganismDomain> {

	@Inject
	private OrganismDAO organismDAO;

	@Override
	public OrganismDomain create(OrganismDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain update(OrganismDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganismDomain delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<OrganismDomain> search(Map<String, Object> searchFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<OrganismDomain> search(Map<String, Object> searchFields, String orderByField) {
		// TODO Auto-generated method stub
		return null;
	}

}
