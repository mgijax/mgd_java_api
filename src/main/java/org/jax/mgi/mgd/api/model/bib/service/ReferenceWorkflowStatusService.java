package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowStatusService extends BaseService<ReferenceWorkflowStatusDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceWorkflowStatusDAO statusDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowStatusTranslator translator = new ReferenceWorkflowStatusTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
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
	public ReferenceWorkflowStatusDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowStatusDomain domain = new ReferenceWorkflowStatusDomain();
		if (statusDAO.get(key) != null) {
			domain = translator.translate(statusDAO.get(key));
		}
		statusDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowStatusDomain> getResults(Integer key) {
        SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();
        results.setItem(translator.translate(statusDAO.get(key)));
        statusDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ReferenceWorkflowStatusDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowStatusDomain> results = new SearchResults<ReferenceWorkflowStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ReferenceWorkflowStatusDomain> search(Integer key) {

		List<ReferenceWorkflowStatusDomain> results = new ArrayList<ReferenceWorkflowStatusDomain>();

		String cmd = "\nselect _refs_key from bib_workflow_statu where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ReferenceWorkflowStatusDomain domain = new ReferenceWorkflowStatusDomain();	
				domain = translator.translate(statusDAO.get(rs.getInt("_refs_key")));
				statusDAO.clear();
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
	public Boolean process(String parentKey, List<ReferenceWorkflowStatusDomain> domain, User user) {
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
				log.info("processWorkflowStatus create");				
				
				// if status is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getStatusKey().isEmpty()) {
					continue;
				}
				
				ReferenceWorkflowStatus entity = new ReferenceWorkflowStatus();						
				entity.setGroupTerm(termDAO.get(Integer.valueOf(domain.get(i).getGroupKey())));		
				entity.setStatusTerm(termDAO.get(Integer.valueOf(domain.get(i).getStatusKey())));
				entity.setIsCurrent(1);
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);
		        entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				statusDAO.persist(entity);				
				modified = true;
				log.info("processWorkflowStatus create successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowStatus delete");
				ReferenceWorkflowStatus entity = statusDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				statusDAO.remove(entity);
				modified = true;
				log.info("processWorkflowStatus delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
				log.info("processWorkflowStatus update");			
				ReferenceWorkflowStatus entity = statusDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				entity.setGroupTerm(termDAO.get(Integer.valueOf(domain.get(i).getGroupKey())));		
				entity.setStatusTerm(termDAO.get(Integer.valueOf(domain.get(i).getStatusKey())));
				entity.setIsCurrent(0);
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				statusDAO.update(entity);				
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
