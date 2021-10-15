package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;

import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;

// DAOs, entities and translators
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.HTExperimentDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;

import org.jax.mgi.mgd.api.model.gxd.translator.HTExperimentTranslator;


import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;

import org.jboss.logging.Logger;

@RequestScoped
public class HTSampleService extends BaseService<HTDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private SQLExecutor sqlExecutor = new SQLExecutor();


	@Inject
	private HTExperimentDAO htExperimentDAO;

// future services
//	@Inject
//	private HTService htService;

	private HTExperimentTranslator translator = new HTExperimentTranslator();

	@Transactional
	public HTDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		HTDomain domain = new HTDomain();
		HTExperiment entity = htExperimentDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}

    @Transactional
    public SearchResults<HTDomain> getResults(Integer key) {
        SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//        results.setItem(translator.translate(htExperimentDAO.get(key)));
        return results;
    } 



	@Transactional
	public SearchResults<HTDomain> delete(Integer key, User user) {
		log.info("HT Sample delete");
		// get the entity object and delete
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//		Assay entity = assayDAO.get(key);
//		results.setItem(translator.translate(htExperimentDAO.get(key)));
//		assayDAO.remove(entity);
		return results;
	}  


	@Transactional
	public SearchResults<HTDomain> update(HTDomain domain, User user) {				
		log.info("HT Sample update");
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		return results;		
	}


	@Transactional
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		log.info("HT Sample create");
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		return results;
	}











	
}
