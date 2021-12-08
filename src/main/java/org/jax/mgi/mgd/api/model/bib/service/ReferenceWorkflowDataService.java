package org.jax.mgi.mgd.api.model.bib.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceWorkflowDataDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowDataDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowData;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceWorkflowDataTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowDataService extends BaseService<ReferenceWorkflowDataDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private ReferenceWorkflowDataDAO wfDataDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowDataTranslator translator = new ReferenceWorkflowDataTranslator();				

	@Transactional
	public SearchResults<ReferenceWorkflowDataDomain> create(ReferenceWorkflowDataDomain domain, User user) {
		SearchResults<ReferenceWorkflowDataDomain> results = new SearchResults<ReferenceWorkflowDataDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowDataDomain> update(ReferenceWorkflowDataDomain domain, User user) {
		SearchResults<ReferenceWorkflowDataDomain> results = new SearchResults<ReferenceWorkflowDataDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceWorkflowDataDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowDataDomain> results = new SearchResults<ReferenceWorkflowDataDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ReferenceWorkflowDataDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowDataDomain domain = new ReferenceWorkflowDataDomain();
		if (wfDataDAO.get(key) != null) {
			domain = translator.translate(wfDataDAO.get(key));
		}
		wfDataDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowDataDomain> getResults(Integer key) {
		SearchResults<ReferenceWorkflowDataDomain> results = new SearchResults<ReferenceWorkflowDataDomain>();
		results.setItem(translator.translate(wfDataDAO.get(key)));
		wfDataDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<ReferenceWorkflowDataDomain> domain, User user) {
		// process workflow data (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processWorkflowData/nothing to process");
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
				
				log.info("processWorkflowData create");

				ReferenceWorkflowData entity = new ReferenceWorkflowData();

				entity.set_refs_key(parentKey);
				
				if (domain.get(i).getHasPDF().equals(true)) {
					entity.setHasPDF(1);
				}
				else {
					entity.setHasPDF(0);
				}
				
				if (domain.get(i).getLinkSupplemental() != null && !domain.get(i).getLinkSupplemental().isEmpty()) {
					entity.setLinkSupplemental(domain.get(i).getLinkSupplemental());
				}
				else {
					entity.setLinkSupplemental(null);
				}
				
				if (domain.get(i).getExtractedText() != null && !domain.get(i).getExtractedText().isEmpty()) {
					entity.setExtractedText(domain.get(i).getExtractedText());
				}
				else {
					entity.setExtractedText(null);
				}
				
				entity.setSupplementalTerm(termDAO.get(Integer.valueOf(domain.get(i).getSupplementalKey())));
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				wfDataDAO.persist(entity);
				
				modified = true;
				log.info("processWorkflowData/create processed: " + entity.get_assoc_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowData delete");			
				ReferenceWorkflowData entity = wfDataDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				wfDataDAO.remove(entity);
				modified = true;
				log.info("processWorkflowData delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processWorkflowData update");

				ReferenceWorkflowData entity = wfDataDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
			
				entity.set_refs_key(parentKey);
				
				if (domain.get(i).getHasPDF().equals(true)) {
					entity.setHasPDF(1);
				}
				else {
					entity.setHasPDF(0);
				}
				
				if (domain.get(i).getLinkSupplemental() != null && !domain.get(i).getLinkSupplemental().isEmpty()) {
					entity.setLinkSupplemental(domain.get(i).getLinkSupplemental());
				}
				else {
					entity.setLinkSupplemental(null);
				}
				
				if (domain.get(i).getExtractedText() != null && !domain.get(i).getExtractedText().isEmpty()) {
					entity.setExtractedText(domain.get(i).getExtractedText());
				}
				else {
					entity.setExtractedText(null);
				}
				
				entity.setSupplementalTerm(termDAO.get(Integer.valueOf(domain.get(i).getSupplementalKey())));				
				entity.setModification_date(new Date());

				wfDataDAO.update(entity);
				
				modified = true;
				log.info("processWorkflowData/changes processed: " + domain.get(i).getAssocKey());	
			}
			else {
				log.info("processWorkflowData/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processWorkflowData/processing successful");
		return modified;
	}
	    
}
