package org.jax.mgi.mgd.api.model.bib.service;

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
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowRelevanceService extends BaseService<ReferenceWorkflowRelevanceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private ReferenceWorkflowRelevanceDAO wfRelevanceDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowRelevanceTranslator translator = new ReferenceWorkflowRelevanceTranslator();				

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
	public SearchResults<ReferenceWorkflowRelevanceDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowRelevanceDomain> results = new SearchResults<ReferenceWorkflowRelevanceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ReferenceWorkflowRelevanceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowRelevanceDomain domain = new ReferenceWorkflowRelevanceDomain();
		if (wfRelevanceDAO.get(key) != null) {
			domain = translator.translate(wfRelevanceDAO.get(key));
		}
		wfRelevanceDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowRelevanceDomain> getResults(Integer key) {
		SearchResults<ReferenceWorkflowRelevanceDomain> results = new SearchResults<ReferenceWorkflowRelevanceDomain>();
		results.setItem(translator.translate(wfRelevanceDAO.get(key)));
		wfRelevanceDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<ReferenceWorkflowRelevanceDomain> domain, User user) {
		// process workflow data (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processWorkflowRelevance/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if ?? is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
//			if (domain.get(i).?? == null || domain.get(i).??.isEmpty()) {
//				continue;
//			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				log.info("processWorkflowRelevance create");

				ReferenceWorkflowRelevance entity = new ReferenceWorkflowRelevance();

				entity.set_refs_key(parentKey);
				
				if (domain.get(i).getIsCurrent().equals(true)) {
					entity.setIsCurrent(1);
				}
				else {
					entity.setIsCurrent(0);
				}
				
				entity.setConfidence(domain.get(i).getConfidence());
				
				if (domain.get(i).getVersion() != null && !domain.get(i).getVersion().isEmpty()) {
					entity.setVersion(domain.get(i).getVersion());
				}
				else {
					entity.setVersion(null);
				}
				
				entity.setRelevanceTerm(termDAO.get(Integer.valueOf(domain.get(i).getRelevanceKey())));
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				wfRelevanceDAO.persist(entity);
				
				modified = true;
				log.info("processWorkflowRelevance/create processed: " + entity.get_assoc_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowRelevance delete");			
				ReferenceWorkflowRelevance entity = wfRelevanceDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				wfRelevanceDAO.remove(entity);
				modified = true;
				log.info("processWorkflowRelevance delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processWorkflowRelevance update");

				ReferenceWorkflowRelevance entity = wfRelevanceDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
			
				entity.set_refs_key(parentKey);
				
				if (domain.get(i).getIsCurrent().equals(true)) {
					entity.setIsCurrent(1);
				}
				else {
					entity.setIsCurrent(0);
				}
				
				entity.setConfidence(domain.get(i).getConfidence());
				
				if (domain.get(i).getVersion() != null && !domain.get(i).getVersion().isEmpty()) {
					entity.setVersion(domain.get(i).getVersion());
				}
				else {
					entity.setVersion(null);
				}
				
				entity.setRelevanceTerm(termDAO.get(Integer.valueOf(domain.get(i).getRelevanceKey())));
				entity.setModification_date(new Date());

				wfRelevanceDAO.update(entity);
				
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
