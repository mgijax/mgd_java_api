package org.jax.mgi.mgd.api.model.mrk.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MarkerService extends BaseService<MarkerDomain> implements BaseSearchInterface<MarkerDomain, MarkerSearchForm> {

	@Inject
	private MarkerDAO markerDAO;

	private MarkerTranslator translator = new MarkerTranslator();
	
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
		return translator.translate(markerDAO.get(key));
	}

	@Override
	public MarkerDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<MarkerDomain> search(MarkerSearchForm searchForm) {
		SearchResults<Marker> markers;
		if(searchForm.getOrderBy() != null) {
			markers = markerDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			markers = markerDAO.search(searchForm.getSearchFields());
		}
		Iterable<MarkerDomain> newItems = translator.translateEntities(markers.items);
		return new SearchResults<MarkerDomain>(newItems);
	}



}
