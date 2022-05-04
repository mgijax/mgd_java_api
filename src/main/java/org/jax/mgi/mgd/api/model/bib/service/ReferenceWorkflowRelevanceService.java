package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceWorkflowRelevanceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowRelevanceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowRelevance;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceWorkflowRelevanceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowRelevanceService extends BaseService<ReferenceWorkflowRelevanceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceWorkflowRelevanceDAO relevanceDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowRelevanceTranslator translator = new ReferenceWorkflowRelevanceTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ReferenceWorkflowRelevanceDomain> create(ReferenceWorkflowRelevanceDomain domain, User user) {
		SearchResults<ReferenceWorkflowRelevanceDomain> results = new SearchResults<ReferenceWorkflowRelevanceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowRelevanceDomain> update(ReferenceWorkflowRelevanceDomain domain, User user) {
		SearchResults<ReferenceWorkflowRelevanceDomain> results = new SearchResults<ReferenceWorkflowRelevanceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ReferenceWorkflowRelevanceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowRelevanceDomain domain = new ReferenceWorkflowRelevanceDomain();
		if (relevanceDAO.get(key) != null) {
			domain = translator.translate(relevanceDAO.get(key));
		}
		relevanceDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowRelevanceDomain> getResults(Integer key) {
        SearchResults<ReferenceWorkflowRelevanceDomain> results = new SearchResults<ReferenceWorkflowRelevanceDomain>();
        results.setItem(translator.translate(relevanceDAO.get(key)));
        relevanceDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ReferenceWorkflowRelevanceDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowRelevanceDomain> results = new SearchResults<ReferenceWorkflowRelevanceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ReferenceWorkflowRelevanceDomain> search(Integer key) {

		List<ReferenceWorkflowRelevanceDomain> results = new ArrayList<ReferenceWorkflowRelevanceDomain>();

		String cmd = "\nselect _refs_key from bib_workflow_relevance where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ReferenceWorkflowRelevanceDomain domain = new ReferenceWorkflowRelevanceDomain();	
				domain = translator.translate(relevanceDAO.get(rs.getInt("_refs_key")));
				relevanceDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@Transactional
	public Boolean process(String parentKey, List<ReferenceWorkflowRelevanceDomain> domain, User user) {
		// process workflow status (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processWorkflowStatus/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
				log.info("processWorkflowRelevance create");				
				
				// if relevance is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getRelevanceKey().isEmpty()) {
					continue;
				}
				
				ReferenceWorkflowRelevance entity = new ReferenceWorkflowRelevance();						

				entity.setRelevanceTerm(termDAO.get(Integer.valueOf(domain.get(i).getRelevanceKey())));
				entity.setIsCurrent(1);
				
				if (domain.get(i).getConfidence() == null || domain.get(i).getConfidence().isEmpty()) {
					entity.setConfidence(null);
				}
				else {
					entity.setConfidence(Double.valueOf(domain.get(i).getConfidence()));					
				}
				
				if (domain.get(i).getVersion() == null || domain.get(i).getVersion().isEmpty()) {
					entity.setVersion(null);
				}
				else {
					entity.setVersion(domain.get(i).getVersion());
				}
				
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);
		        entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				relevanceDAO.persist(entity);				
				modified = true;
				log.info("processWorkflowRelevance create successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowRelevance delete");
				ReferenceWorkflowRelevance entity = relevanceDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				relevanceDAO.remove(entity);
				modified = true;
				log.info("processWorkflowRelevance delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
				log.info("processWorkflowRelevance update");			
				ReferenceWorkflowRelevance entity = relevanceDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				entity.setRelevanceTerm(termDAO.get(Integer.valueOf(domain.get(i).getRelevanceKey())));
				entity.setIsCurrent(Integer.valueOf(domain.get(i).getIsCurrent()));

				if (domain.get(i).getConfidence() == null || domain.get(i).getConfidence().isEmpty()) {
					entity.setConfidence(null);
				}
				else {
					entity.setConfidence(Double.valueOf(domain.get(i).getConfidence()));					
				}
				
				if (domain.get(i).getVersion() == null || domain.get(i).getVersion().isEmpty()) {
					entity.setVersion(null);
				}
				else {
					entity.setVersion(domain.get(i).getVersion());
				}
				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				relevanceDAO.update(entity);				
				modified = true;
				log.info("processWorkflowRelevance/changes processed: " + domain.get(i).getAssocKey());
			}
			else {
				log.info("processWorkflowRelevance/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processWorkflowRelevance/processing successful");
		return modified;
	}
	
}
