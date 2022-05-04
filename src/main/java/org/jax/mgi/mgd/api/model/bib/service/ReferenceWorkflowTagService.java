package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceWorkflowTagService extends BaseService<ReferenceWorkflowTagDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceWorkflowTagDAO tagDAO;
	@Inject
	private TermDAO termDAO;
	
	private ReferenceWorkflowTagTranslator translator = new ReferenceWorkflowTagTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
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
	public ReferenceWorkflowTagDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceWorkflowTagDomain domain = new ReferenceWorkflowTagDomain();
		if (tagDAO.get(key) != null) {
			domain = translator.translate(tagDAO.get(key));
		}
		tagDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceWorkflowTagDomain> getResults(Integer key) {
        SearchResults<ReferenceWorkflowTagDomain> results = new SearchResults<ReferenceWorkflowTagDomain>();
        results.setItem(translator.translate(tagDAO.get(key)));
        tagDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ReferenceWorkflowTagDomain> delete(Integer key, User user) {
		SearchResults<ReferenceWorkflowTagDomain> results = new SearchResults<ReferenceWorkflowTagDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ReferenceWorkflowTagDomain> search(Integer key) {

		List<ReferenceWorkflowTagDomain> results = new ArrayList<ReferenceWorkflowTagDomain>();

		String cmd = "\nselect _refs_key from bib_workflow_tag where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ReferenceWorkflowTagDomain domain = new ReferenceWorkflowTagDomain();	
				domain = translator.translate(tagDAO.get(rs.getInt("_refs_key")));
				tagDAO.clear();
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
	public Boolean process(String parentKey, List<ReferenceWorkflowTagDomain> domain, User user) {
		// process workflow status (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processWorkflowTag/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
				log.info("processWorkflowTag create");				
				
				// if tag is empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getTagKey().isEmpty()) {
					continue;
				}
				
				ReferenceWorkflowTag entity = new ReferenceWorkflowTag();						
				entity.setTagTerm(termDAO.get(Integer.valueOf(domain.get(i).getTagKey())));
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);
		        entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				tagDAO.persist(entity);				
				modified = true;
				log.info("processWorkflowTag create successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processWorkflowTag delete");
				ReferenceWorkflowTag entity = tagDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				tagDAO.remove(entity);
				modified = true;
				log.info("processWorkflowTag delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {								
				log.info("processWorkflowTag update");			
				ReferenceWorkflowTag entity = tagDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				entity.setTagTerm(termDAO.get(Integer.valueOf(domain.get(i).getTagKey())));
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				tagDAO.update(entity);				
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
