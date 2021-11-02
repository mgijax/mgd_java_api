package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.domain.HTRawSampleDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.HTRawSampleDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTRawSample;
import org.jax.mgi.mgd.api.model.gxd.translator.HTRawSampleTranslator;

import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jax.mgi.mgd.api.util.Constants;

import org.jboss.logging.Logger;

@RequestScoped
public class HTRawSampleService extends BaseService<HTRawSampleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Inject
	private HTRawSampleDAO htRawSampleDAO;

	private HTRawSampleTranslator translator = new HTRawSampleTranslator();

	@Transactional
	public HTRawSampleDomain get(Integer key) {
		log.info("HT Raw Sample get");
		// get the DAO/entity and translate -> domain
		HTRawSampleDomain domain = new HTRawSampleDomain();
		HTRawSample entity = htRawSampleDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}

    @Transactional
    public SearchResults<HTRawSampleDomain> getResults(Integer key) {
		log.info("HT Raw Sample getResults");
        SearchResults<HTRawSampleDomain> results = new SearchResults<HTRawSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 

	@Transactional
	public SearchResults<HTRawSampleDomain> delete(Integer key, User user) {
		log.info("HT Raw Sample delete");
		SearchResults<HTRawSampleDomain> results = new SearchResults<HTRawSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}  

	@Transactional
	public SearchResults<HTRawSampleDomain> update(HTRawSampleDomain domain, User user) {				
		log.info("HT Raw Sample update");
		SearchResults<HTRawSampleDomain> results = new SearchResults<HTRawSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}

	@Transactional
	public SearchResults<HTRawSampleDomain> create(HTRawSampleDomain domain, User user) {
		log.info("HT Raw Sample create");
		SearchResults<HTRawSampleDomain> results = new SearchResults<HTRawSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

}
