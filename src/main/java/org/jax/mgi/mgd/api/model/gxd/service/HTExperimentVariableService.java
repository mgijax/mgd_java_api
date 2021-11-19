package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.HTExperimentVariableDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperimentVariable;
import org.jax.mgi.mgd.api.model.gxd.translator.HTExperimentVariableTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class HTExperimentVariableService extends BaseService<HTExperimentVariableDomain> {

	protected Logger log = Logger.getLogger(getClass());
	//private SQLExecutor sqlExecutor = new SQLExecutor();

	@Inject
	private HTExperimentVariableDAO htExperimentVariableDAO;
	private HTExperimentVariableTranslator translator = new HTExperimentVariableTranslator();

	@Transactional
	public SearchResults<HTExperimentVariableDomain> create(HTExperimentVariableDomain domain, User user) {
		log.info("HTExperimentVariableService create");
		SearchResults<HTExperimentVariableDomain> results = new SearchResults<HTExperimentVariableDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<HTExperimentVariableDomain> update(HTExperimentVariableDomain domain, User user) {				
		log.info("HTExperimentVariableService update");
		SearchResults<HTExperimentVariableDomain> results = new SearchResults<HTExperimentVariableDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}
	
	@Transactional
	public SearchResults<HTExperimentVariableDomain> delete(Integer key, User user) {
		log.info("HTExperimentVariableService delete");
		SearchResults<HTExperimentVariableDomain> results = new SearchResults<HTExperimentVariableDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	} 
	
	@Transactional
	public HTExperimentVariableDomain get(Integer key) {
		log.info("HTExperimentVariableService get (by key)");
		HTExperimentVariableDomain domain = new HTExperimentVariableDomain();
		HTExperimentVariable entity = htExperimentVariableDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}
	
    @Transactional
    public SearchResults<HTExperimentVariableDomain> getResults(Integer key) {
		log.info("HTExperimentVariableService getResults");
        SearchResults<HTExperimentVariableDomain> results = new SearchResults<HTExperimentVariableDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
}
