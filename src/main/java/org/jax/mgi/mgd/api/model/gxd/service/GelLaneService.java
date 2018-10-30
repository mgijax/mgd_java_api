package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.GelLaneDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;
import org.jax.mgi.mgd.api.model.gxd.search.GelLaneSearchForm;
import org.jax.mgi.mgd.api.model.gxd.translator.GelLaneTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class GelLaneService extends BaseService<GelLaneDomain> implements BaseSearchInterface<GelLaneDomain, GelLaneSearchForm> {

	@Inject
	private GelLaneDAO gelaneDAO;

	private GelLaneTranslator translator = new GelLaneTranslator();
	
	@Transactional
	public GelLaneDomain create(GelLaneDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	public GelLaneDomain update(GelLaneDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public GelLaneDomain get(Integer key) {
		return translator.translate(gelaneDAO.get(key));
	}

	@Transactional
	public SearchResults<GelLaneDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<GelLaneDomain> search(GelLaneSearchForm searchForm) {
		SearchResults<GelLane> gelanes;
		if(searchForm.getOrderBy() != null) {
			gelanes = gelaneDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			gelanes = gelaneDAO.search(searchForm.getSearchFields());
		}
		Iterable<GelLaneDomain> newItems = translator.translateEntities(gelanes.items, searchForm.getSearchDepth());
		return new SearchResults<GelLaneDomain>(newItems);
	}




}
