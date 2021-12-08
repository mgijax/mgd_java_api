package org.jax.mgi.mgd.api.model.bib.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceWorkflowTagDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowTagDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowTag;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceWorkflowTagTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowTagService extends BaseService<ReferenceWorkflowTagDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private ReferenceWorkflowTagDAO wfTagDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowTagTranslator translator = new ReferenceWorkflowTagTranslator();				

	@Transactional
	public SearchResults<ReferenceWorkflowTagDomain> create(ReferenceWorkflowTagDomain domain, User user) {
		SearchResults<ReferenceWorkflowTagDomain> results = new SearchResults<ReferenceWorkflowTagDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowTagDomain> update(ReferenceWorkflowTagDomain domain, User user) {
		SearchResults<ReferenceWorkflowTagDomain> results = new SearchResults<ReferenceWorkflowTagDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowTagDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowTagDomain> results = new SearchResults<ReferenceWorkflowTagDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ReferenceWorkflowTagDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowTagDomain domain = new ReferenceWorkflowTagDomain();
		if (wfTagDAO.get(key) != null) {
			domain = translator.translate(wfTagDAO.get(key));
		}
		wfTagDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowTagDomain> getResults(Integer key) {
		SearchResults<ReferenceWorkflowTagDomain> results = new SearchResults<ReferenceWorkflowTagDomain>();
		results.setItem(translator.translate(wfTagDAO.get(key)));
		wfTagDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<ReferenceWorkflowTagDomain> domain, User user) {
		// process workflow data (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processWorkflowTag/nothing to process");
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
				
				log.info("processWorkflowTag create");

				ReferenceWorkflowTag entity = new ReferenceWorkflowTag();

				entity.set_refs_key(parentKey);			
				entity.setTagTerm(termDAO.get(Integer.valueOf(domain.get(i).getTagKey())));
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				wfTagDAO.persist(entity);
				
				modified = true;
				log.info("processWorkflowTag/create processed: " + entity.get_assoc_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowTag delete");			
				ReferenceWorkflowTag entity = wfTagDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				wfTagDAO.remove(entity);
				modified = true;
				log.info("processWorkflowTag delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processWorkflowTag update");

				ReferenceWorkflowTag entity = wfTagDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
			
				entity.set_refs_key(parentKey);
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());

				wfTagDAO.update(entity);
				
				modified = true;
				log.info("processWorkflowTag/changes processed: " + domain.get(i).getAssocKey());	
			}
			else {
				log.info("processWorkflowTag/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processWorkflowTag/processing successful");
		return modified;
	}
	    
}
