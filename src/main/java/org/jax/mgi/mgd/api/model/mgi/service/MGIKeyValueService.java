package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIKeyValueDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIKeyValueDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIKeyValue;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIKeyValueTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIKeyValueService extends BaseService<MGIKeyValueDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGIKeyValueDAO mgiKeyValueDAO;
	
	private MGIKeyValueTranslator translator = new MGIKeyValueTranslator();

	@Transactional
	public MGIKeyValueDomain get(Integer key) {
		log.info("MGIKeyValue get");
		// get the DAO/entity and translate -> domain
		MGIKeyValueDomain domain = new MGIKeyValueDomain();
		MGIKeyValue entity = mgiKeyValueDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}

    @Transactional
    public SearchResults<MGIKeyValueDomain> getResults(Integer key) {
		log.info("MGIKeyValue getResults");
        SearchResults<MGIKeyValueDomain> results = new SearchResults<MGIKeyValueDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 

	@Transactional
	public SearchResults<MGIKeyValueDomain> delete(Integer key, User user) {
		log.info("MGIKeyValue delete");
		SearchResults<MGIKeyValueDomain> results = new SearchResults<MGIKeyValueDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}  

	@Transactional
	public SearchResults<MGIKeyValueDomain> update(MGIKeyValueDomain domain, User user) {				
		log.info("MGIKeyValue update");
		SearchResults<MGIKeyValueDomain> results = new SearchResults<MGIKeyValueDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}

	@Transactional
	public SearchResults<MGIKeyValueDomain> create(MGIKeyValueDomain domain, User user) {
		log.info("MGIKeyValue create");
		SearchResults<MGIKeyValueDomain> results = new SearchResults<MGIKeyValueDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	

	
}
