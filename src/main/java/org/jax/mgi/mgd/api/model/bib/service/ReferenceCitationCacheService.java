package org.jax.mgi.mgd.api.model.bib.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceCitationCacheDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceCitationCacheTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceCitationCacheService extends BaseService<ReferenceCitationCacheDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceCitationCacheDAO referenceDAO;

	private ReferenceCitationCacheTranslator translator = new ReferenceCitationCacheTranslator();

	@Transactional
	public SearchResults<ReferenceCitationCacheDomain> create(ReferenceCitationCacheDomain object, User user) {
		SearchResults<ReferenceCitationCacheDomain> results = new SearchResults<ReferenceCitationCacheDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ReferenceCitationCacheDomain> update(ReferenceCitationCacheDomain object, User user) {
		SearchResults<ReferenceCitationCacheDomain> results = new SearchResults<ReferenceCitationCacheDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceCitationCacheDomain> delete(Integer key, User user) {
		SearchResults<ReferenceCitationCacheDomain> results = new SearchResults<ReferenceCitationCacheDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ReferenceCitationCacheDomain get(Integer key) {
		ReferenceCitationCacheDomain domain = new ReferenceCitationCacheDomain();
		if (referenceDAO.get(key) != null) {
			domain = translator.translate(referenceDAO.get(key));
		}
		referenceDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceCitationCacheDomain> getResults(Integer key) {
        SearchResults<ReferenceCitationCacheDomain> results = new SearchResults<ReferenceCitationCacheDomain>();
        results.setItem(translator.translate(referenceDAO.get(key)));
        referenceDAO.clear();
        return results;
    }

	@Transactional	
	public List<ReferenceCitationCacheDomain> search(ReferenceCitationCacheDomain searchDomain) {
		List<ReferenceCitationCacheDomain> results = new ArrayList<ReferenceCitationCacheDomain>();
		return results;
	}	
	
}
