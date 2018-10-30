package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.IndexDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Index;
import org.jax.mgi.mgd.api.model.gxd.search.IndexSearchForm;
import org.jax.mgi.mgd.api.model.gxd.translator.IndexTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class IndexService extends BaseService<IndexDomain> implements BaseSearchInterface<IndexDomain, IndexSearchForm> {

	@Inject
	private IndexDAO indexDAO;

	private IndexTranslator translator = new IndexTranslator();
	
	@Transactional
	public IndexDomain create(IndexDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	public IndexDomain update(IndexDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public IndexDomain get(Integer key) {
		return translator.translate(indexDAO.get(key));
	}

	@Transactional
	public SearchResults<IndexDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<IndexDomain> search(IndexSearchForm searchForm) {
		SearchResults<Index> indexs;
		if(searchForm.getOrderBy() != null) {
			indexs = indexDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			indexs = indexDAO.search(searchForm.getSearchFields());
		}
		Iterable<IndexDomain> newItems = translator.translateEntities(indexs.items, searchForm.getSearchDepth());
		return new SearchResults<IndexDomain>(newItems);
	}




}
