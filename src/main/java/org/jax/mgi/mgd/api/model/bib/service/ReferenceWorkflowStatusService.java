package org.jax.mgi.mgd.api.model.bib.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceWorkflowStatusDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowStatus;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceWorkflowStatusTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowStatusService extends BaseService<ReferenceWorkflowStatusDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private ReferenceWorkflowStatusDAO wfStatusDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowStatusTranslator translator = new ReferenceWorkflowStatusTranslator();				

	@Transactional
	public SearchResults<ReferenceWorkflowStatusDomain> create(ReferenceWorkflowStatusDomain domain, User user) {
		SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowStatusDomain> update(ReferenceWorkflowStatusDomain domain, User user) {
		SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowStatusDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ReferenceWorkflowStatusDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowStatusDomain domain = new ReferenceWorkflowStatusDomain();
		if (wfStatusDAO.get(key) != null) {
			domain = translator.translate(wfStatusDAO.get(key));
		}
		wfStatusDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowStatusDomain> getResults(Integer key) {
		SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();
		results.setItem(translator.translate(wfStatusDAO.get(key)));
		wfStatusDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<ReferenceWorkflowStatusDomain> domain, User user) {
		// process workflow data (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processWorkflowStatus/nothing to process");
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
				
				log.info("processWorkflowStatus create");

				ReferenceWorkflowStatus entity = new ReferenceWorkflowStatus();

				entity.set_refs_key(parentKey);
				
				if (domain.get(i).getIsCurrent().equals(true)) {
					entity.setIsCurrent(1);
				}
				else {
					entity.setIsCurrent(0);
				}
				
				entity.setGroupTerm(termDAO.get(Integer.valueOf(domain.get(i).getGroupKey())));
				entity.setStatusTerm(termDAO.get(Integer.valueOf(domain.get(i).getStatusKey())));
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				wfStatusDAO.persist(entity);
				
				modified = true;
				log.info("processWorkflowStatus/create processed: " + entity.get_assoc_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowStatus delete");			
				ReferenceWorkflowStatus entity = wfStatusDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				wfStatusDAO.remove(entity);
				modified = true;
				log.info("processWorkflowStatus delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processWorkflowStatus update");

				ReferenceWorkflowStatus entity = wfStatusDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
			
				entity.set_refs_key(parentKey);
				
				if (domain.get(i).getIsCurrent().equals(true)) {
					entity.setIsCurrent(1);
				}
				else {
					entity.setIsCurrent(0);
				}
				
				entity.setGroupTerm(termDAO.get(Integer.valueOf(domain.get(i).getGroupKey())));
				entity.setStatusTerm(termDAO.get(Integer.valueOf(domain.get(i).getStatusKey())));				
				entity.setModification_date(new Date());

				wfStatusDAO.update(entity);
				
				modified = true;
				log.info("processWorkflowStatus/changes processed: " + domain.get(i).getAssocKey());	
			}
			else {
				log.info("processWorkflowStatus/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processWorkflowStatus/processing successful");
		return modified;
	}
	    
}
