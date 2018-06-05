package org.jax.mgi.mgd.api.model.prb.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeMarkerDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.model.prb.search.ProbeMarkerSearchForm;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeMarkerTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ProbeMarkerService extends BaseService<ProbeMarkerDomain> implements BaseSearchInterface<ProbeMarkerDomain, ProbeMarkerSearchForm> {

	@Inject
	private ProbeMarkerDAO probeMarkerDAO;

	private ProbeMarkerTranslator translator = new ProbeMarkerTranslator();
	
	@Transactional
	public ProbeMarkerDomain create(ProbeMarkerDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ProbeMarkerDomain update(ProbeMarkerDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ProbeMarkerDomain get(Integer key) {
		return translator.translate(probeMarkerDAO.get(key));
	}

	@Transactional
	public ProbeMarkerDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ProbeMarkerDomain> search(ProbeMarkerSearchForm searchForm) {
		SearchResults<ProbeMarker> probeMarkers;
		if(searchForm.getOrderBy() != null) {
			probeMarkers = probeMarkerDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			probeMarkers = probeMarkerDAO.search(searchForm.getSearchFields());
		}
		Iterable<ProbeMarkerDomain> newItems = translator.translateEntities(probeMarkers.items, searchForm.getSearchDepth());
		return new SearchResults<ProbeMarkerDomain>(newItems);
	}



}
