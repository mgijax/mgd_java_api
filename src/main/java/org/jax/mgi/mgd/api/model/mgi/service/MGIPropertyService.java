package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIPropertyDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.PropertyTypeDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIPropertyTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIPropertyService extends BaseService<MGIPropertyDomain> {

	protected Logger log = Logger.getLogger(getClass()); 
	
	@Inject
	private MGIPropertyDAO propertyDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private PropertyTypeDAO propertyTypeDAO;
	@Inject
	private MGITypeDAO mgiTypeDAO;

	private MGIPropertyTranslator translator = new MGIPropertyTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGIPropertyDomain> create(MGIPropertyDomain object, User user) {
		SearchResults<MGIPropertyDomain> results = new SearchResults<MGIPropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGIPropertyDomain> update(MGIPropertyDomain object, User user) {
		SearchResults<MGIPropertyDomain> results = new SearchResults<MGIPropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGIPropertyDomain> delete(Integer key, User user) {
		SearchResults<MGIPropertyDomain> results = new SearchResults<MGIPropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGIPropertyDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGIPropertyDomain domain = new MGIPropertyDomain();
		if (propertyDAO.get(key) != null) {
			domain = translator.translate(propertyDAO.get(key));
		}
		propertyDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MGIPropertyDomain> getResults(Integer key) {
		SearchResults<MGIPropertyDomain> results = new SearchResults<MGIPropertyDomain>();
		results.setItem(translator.translate(propertyDAO.get(key)));
		propertyDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(List<MGIPropertyDomain> domain, User user) {
		// process property associations (create, delete, update)
		log.info("processMGIProperty/start");
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processMGIProperty/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		for (int i = 0; i < domain.size(); i++) {
			log.info("--- :" + domain.get(i).getProcessStatus());
		
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				log.info("processMGIProperty create");

				// if MGIProperty is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getValue() == null || domain.get(i).getValue().isEmpty()) {
					log.info("no MGIProperty to create");
					continue;
				}
				
				MGIProperty entity = new MGIProperty();
				
				entity.set_object_key(Integer.parseInt(domain.get(i).getObjectKey()));
				entity.setValue(domain.get(i).getValue());
				entity.setSequenceNum(1);

				entity.setMgitype(mgiTypeDAO.get(Integer.parseInt(domain.get(i).getMgiTypeKey())));
				entity.setPropertyTerm(termDAO.get(Integer.parseInt(domain.get(i).getPropertyTermKey())));
				entity.setPropertyType(propertyTypeDAO.get(Integer.parseInt(domain.get(i).getPropertyTypeKey())));


				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);

				log.info("processMGIProperty/ updating entity");					
				propertyDAO.update(entity);
				//propertyDAO.persist(entity);

				log.info("processMGIProperty/changes processed: ");	
			}

			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processHTSample delete");
				MGIProperty entity = propertyDAO.get(Integer.valueOf(domain.get(i).getPropertyKey()));
				propertyDAO.remove(entity);
				log.info("processHTSample delete successful");
			}

			else {
				log.info("processMGIProperty/no changes processed: ");
			}
		}
		
		log.info("processMGIProperty/processing successful");
		return modified;
	}
	
}
