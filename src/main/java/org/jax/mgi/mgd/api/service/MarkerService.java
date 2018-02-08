package org.jax.mgi.mgd.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MarkerService extends ServiceInterface<MarkerDomain> {

	@Inject
	private MarkerDAO markerDAO;

	@Override
	public MarkerDomain create(MarkerDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkerDomain update(MarkerDomain object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkerDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkerDomain delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<MarkerDomain> search(Map<String, Object> searchFields) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<MarkerDomain> search(Map<String, Object> searchFields, String orderByField) {
		// TODO Auto-generated method stub
		return null;
	}




}
