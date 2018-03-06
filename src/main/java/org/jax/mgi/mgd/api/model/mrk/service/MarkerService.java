package org.jax.mgi.mgd.api.model.mrk.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.service.ServiceInterface;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MarkerService extends ServiceInterface<MarkerDomain, MarkerSearchForm> {

	@Inject
	private MarkerDAO markerDAO;

	@Override
	public MarkerDomain create(MarkerDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkerDomain update(MarkerDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkerDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkerDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<MarkerDomain> search(MarkerSearchForm searchForm) {
		// TODO Auto-generated method stub
		return null;
	}



}
