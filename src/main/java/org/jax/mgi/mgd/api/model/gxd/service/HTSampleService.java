package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.HTSampleDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.gxd.translator.HTSampleTranslator;

import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jax.mgi.mgd.api.util.Constants;

import org.jboss.logging.Logger;

@RequestScoped
public class HTSampleService extends BaseService<HTSampleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Inject
	private HTSampleDAO htSampleDAO;

	private HTSampleTranslator translator = new HTSampleTranslator();

	@Transactional
	public HTSampleDomain get(Integer key) {
		log.info("HT Sample get");
		// get the DAO/entity and translate -> domain
		HTSampleDomain domain = new HTSampleDomain();
		HTSample entity = htSampleDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}

    @Transactional
    public SearchResults<HTSampleDomain> getResults(Integer key) {
		log.info("HT Sample getResults");
        SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 

	@Transactional
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		log.info("HT Sample delete");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}  

	@Transactional
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {				
		log.info("HT Sample update");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}

	@Transactional
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		log.info("HT Sample create");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

}
