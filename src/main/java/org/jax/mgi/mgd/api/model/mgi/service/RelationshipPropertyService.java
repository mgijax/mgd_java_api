package org.jax.mgi.mgd.api.model.mgi.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.RelationshipPropertyDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipPropertyTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class RelationshipPropertyService extends BaseService<RelationshipPropertyDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private RelationshipPropertyDAO relationshipPropertyDAO;
	@Inject
	private TermDAO termDAO;
	
	private RelationshipPropertyTranslator translator = new RelationshipPropertyTranslator();
	
	@Transactional
	public SearchResults<RelationshipPropertyDomain> create(RelationshipPropertyDomain object, User user) {
		SearchResults<RelationshipPropertyDomain> results = new SearchResults<RelationshipPropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<RelationshipPropertyDomain> update(RelationshipPropertyDomain object, User user) {
		SearchResults<RelationshipPropertyDomain> results = new SearchResults<RelationshipPropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<RelationshipPropertyDomain> delete(Integer key, User user) {
		SearchResults<RelationshipPropertyDomain> results = new SearchResults<RelationshipPropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public RelationshipPropertyDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		RelationshipPropertyDomain domain = new RelationshipPropertyDomain();
		if (relationshipPropertyDAO.get(key) != null) {
			domain = translator.translate(relationshipPropertyDAO.get(key));
		}
		relationshipPropertyDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<RelationshipPropertyDomain> getResults(Integer key) {
        SearchResults<RelationshipPropertyDomain> results = new SearchResults<RelationshipPropertyDomain>();
        results.setItem(translator.translate(relationshipPropertyDAO.get(key)));
        relationshipPropertyDAO.clear();
        return results;
    }
	
	@Transactional
	public Boolean process(List<RelationshipPropertyDomain> domain, String parentKey, User user) {
		// process relationship properties (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processRelationshipProperty/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
				log.info("processRelationshipProperty create");				
				
				// if value is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getValue().isEmpty()) {
					continue;
				}
				
				RelationshipProperty entity = new RelationshipProperty();
				entity.set_relationship_key(Integer.valueOf(parentKey));
				entity.setPropertyName(termDAO.get(Integer.valueOf(domain.get(i).getPropertyNameKey())));
				entity.setValue(domain.get(i).getValue());
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);
		        entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				relationshipPropertyDAO.persist(entity);				
				modified = true;
				log.info("processRelationshipProperty create successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processRelationshipProperty delete");
				RelationshipProperty entity = relationshipPropertyDAO.get(Integer.valueOf(domain.get(i).getRelationshipPropertyKey()));
				relationshipPropertyDAO.remove(entity);
				modified = true;
				log.info("processRelationshipProperty delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
				log.info("processRelationshipProperty update");			
				RelationshipProperty entity = relationshipPropertyDAO.get(Integer.valueOf(domain.get(i).getRelationshipPropertyKey()));
				entity.set_relationship_key(Integer.valueOf(parentKey));
				entity.setPropertyName(termDAO.get(Integer.valueOf(domain.get(i).getPropertyNameKey())));
				entity.setValue(domain.get(i).getValue());
				entity.setSequenceNum(domain.get(i).getSequenceNum());				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				relationshipPropertyDAO.update(entity);
				modified = true;
				log.info("processRelationshipProperty/changes processed: " + domain.get(i).getRelationshipKey());
			}
			else {
				log.info("processRelationshipProperty/no changes processed: " + domain.get(i).getRelationshipKey());
			}
		}
		
		log.info("processRelationshipProperty/processing successful");
		return modified;
	}
	
}
