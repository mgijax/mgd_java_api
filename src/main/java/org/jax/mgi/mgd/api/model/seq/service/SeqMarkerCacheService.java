package org.jax.mgi.mgd.api.model.seq.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.dao.SeqMarkerCacheDAO;
import org.jax.mgi.mgd.api.model.seq.domain.SeqMarkerCacheDomain;
import org.jax.mgi.mgd.api.model.seq.entities.SeqMarkerCache;
import org.jax.mgi.mgd.api.model.seq.search.SeqMarkerCacheSearchForm;
import org.jax.mgi.mgd.api.model.seq.translator.SeqMarkerCacheTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class SeqMarkerCacheService extends BaseService<SeqMarkerCacheDomain> implements BaseSearchInterface<SeqMarkerCacheDomain, SeqMarkerCacheSearchForm> {

	@Inject
	private SeqMarkerCacheDAO seqmarkercacheDAO;

	private SeqMarkerCacheTranslator translator = new SeqMarkerCacheTranslator();
	
	@Transactional
	public SeqMarkerCacheDomain create(SeqMarkerCacheDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SeqMarkerCacheDomain update(SeqMarkerCacheDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SeqMarkerCacheDomain get(Integer key) {
		return translator.translate(seqmarkercacheDAO.get(key),3);
	}

	@Transactional
	public SeqMarkerCacheDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<SeqMarkerCacheDomain> search(SeqMarkerCacheSearchForm searchForm) {
		SearchResults<SeqMarkerCache> seqmarkercaches;
		if(searchForm.getOrderBy() != null) {
			seqmarkercaches = seqmarkercacheDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			seqmarkercaches = seqmarkercacheDAO.search(searchForm.getSearchFields());
		}
		Iterable<SeqMarkerCacheDomain> newItems = translator.translateEntities(seqmarkercaches.items, searchForm.getSearchDepth());
		return new SearchResults<SeqMarkerCacheDomain>(newItems);
	}



}
