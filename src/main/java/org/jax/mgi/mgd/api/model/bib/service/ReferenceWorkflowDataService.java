package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowDataService extends BaseService<ReferenceWorkflowDataDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceWorkflowDataDAO dataDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowDataTranslator translator = new ReferenceWorkflowDataTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
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
	public ReferenceWorkflowDataDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowDataDomain domain = new ReferenceWorkflowDataDomain();
		if (dataDAO.get(key) != null) {
			domain = translator.translate(dataDAO.get(key));
		}
		dataDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowDataDomain> getResults(Integer key) {
        SearchResults<ReferenceWorkflowDataDomain> results = new SearchResults<ReferenceWorkflowDataDomain>();
        results.setItem(translator.translate(dataDAO.get(key)));
        dataDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ReferenceWorkflowDataDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowDataDomain> results = new SearchResults<ReferenceWorkflowDataDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ReferenceWorkflowDataDomain> search(Integer key) {

		List<ReferenceWorkflowDataDomain> results = new ArrayList<ReferenceWorkflowDataDomain>();

		String cmd = "\nselect _refs_key from bib_workflow_data where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ReferenceWorkflowDataDomain domain = new ReferenceWorkflowDataDomain();	
				domain = translator.translate(dataDAO.get(rs.getInt("_refs_key")));
				dataDAO.clear();
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
	public Boolean process(String parentKey, ReferenceWorkflowDataDomain domain, User user) {
		// process workflow status (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null) {
			log.info("processWorkflowData/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processWorkflowData create");				
				
			// if extractedtext is empty, then skip
			// pwi has sent a "c" that is empty/not being used
//			if (domain.get(i).getExtractedTextKey().isEmpty()) {
//				continue;
//			}
				
			ReferenceWorkflowData entity = new ReferenceWorkflowData();				
			entity.setHaspdf(0);
			entity.setSupplementalTerm(termDAO.get(Integer.valueOf(domain.getSupplementalKey())));
			entity.setExtractedTextTerm(termDAO.get(48804490)); // "body"
			entity.setExtractedtext(null);				
			entity.setLinksupplemental(null);
			entity.setCreation_date(new Date());
			entity.setCreatedBy(user);
		    entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			dataDAO.persist(entity);				
			modified = true;
			log.info("processWorkflowData create successful");
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processWorkflowData delete");
			ReferenceWorkflowData entity = dataDAO.get(Integer.valueOf(domain.getAssocKey()));
			dataDAO.remove(entity);
			modified = true;
			log.info("processWorkflowData delete successful");
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
			log.info("processWorkflowData update");			
			ReferenceWorkflowData entity = dataDAO.get(Integer.valueOf(domain.getAssocKey()));
			entity.setSupplementalTerm(termDAO.get(Integer.valueOf(domain.getSupplementalKey())));
			entity.setModifiedBy(user);
		    entity.setModification_date(new Date());
			// don't update these fields from API
//			entity.setHaspdf(0);
//			entity.setExtractedtext(domain.get(i).getExtractedtext());
//			entity.setExtractedTextTerm(termDAO.get(Integer.valueOf(domain.get(i).getExtractedTextKey())));
//			entity.setLinksupplemental(null);
			dataDAO.update(entity);				
			modified = true;
			log.info("processWorkflowData/changes processed: " + domain.getAssocKey());
		}
		else {
			log.info("processWorkflowData/no changes processed: " + domain.getAssocKey());
		}
		
		log.info("processWorkflowData/processing successful");
		return modified;
	}
	
}
