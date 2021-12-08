package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.HTExperimentVariableDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.HTExperimentDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperimentVariable;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;
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
	@Inject
	private HTExperimentDAO htExperimentDAO;
	@Inject
	private TermDAO termDAO;

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

	@Transactional
	public Boolean process(Integer parentKey, List<HTExperimentVariableDomain> domain, User user) {

		// This method is created to mirror pre-existing python API functionality, and matches
		// what the HT module .js is expecting
		
		Boolean modified = false;

		if (domain == null || domain.isEmpty()) {
			log.info("process HTExperiment Variable / nothing to process");
			return modified;
		}
		modified = true;

		// Remove any existing variables
		HTExperiment experimentEntity = htExperimentDAO.get(parentKey);
		List<HTExperimentVariable> existingVariables = experimentEntity.getExperiment_variables();
		for (int i = 0; i < existingVariables.size(); i++) {
			log.info("process HTExperiment Variable delete");
			HTExperimentVariable oldEntity = htExperimentVariableDAO.get(existingVariables.get(i).get_experimentvariable_key());
			htExperimentVariableDAO.remove(oldEntity);
		}

		// add each experiment variable passed from web (including "Not Curated")
		for (int i = 0; i < domain.size(); i++) {
			HTExperimentVariable newEntity = new HTExperimentVariable();
			newEntity.set_experiment_key(parentKey);
			newEntity.setTerm(termDAO.get(domain.get(i).get_term_key()));
			htExperimentVariableDAO.update(newEntity);
		}
		
		log.info("processHTExperimentVariable processing successful");
		return modified;
	}
	
}







