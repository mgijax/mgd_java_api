package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;

import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;

// DAOs, entities and translators
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.HTSampleDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;

import org.jax.mgi.mgd.api.model.gxd.translator.HTSampleTranslator;


import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;

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
        SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
//        results.setItem(translator.translate(htSampleDAO.get(key)));
        return results;
    } 



	@Transactional
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		log.info("HT Sample delete");
		// get the entity object and delete
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
//		HTSample entity = htSampleDAO.get(key);
//		results.setItem(translator.translate(htSampleDAO.get(key)));
//		htSampleDAO.remove(entity);
		return results;
	}  


	@Transactional
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {				
		log.info("HT Sample update");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		return results;		
	}


	@Transactional
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		log.info("HT Sample create");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		return results;
	}











	
}
