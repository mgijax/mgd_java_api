package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GXDIndexStageDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexStageDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndexStage;
import org.jax.mgi.mgd.api.model.gxd.translator.GXDIndexStageTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GXDIndexStageService extends BaseService<GXDIndexStageDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GXDIndexStageDAO stageDAO;
	@Inject
	private TermDAO termDAO;
	
	private GXDIndexStageTranslator translator = new GXDIndexStageTranslator();				

	@Transactional
	public SearchResults<GXDIndexStageDomain> create(GXDIndexStageDomain domain, User user) {
		SearchResults<GXDIndexStageDomain> results = new SearchResults<GXDIndexStageDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GXDIndexStageDomain> update(GXDIndexStageDomain domain, User user) {
		SearchResults<GXDIndexStageDomain> results = new SearchResults<GXDIndexStageDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GXDIndexStageDomain> delete(Integer key, User user) {
		SearchResults<GXDIndexStageDomain> results = new SearchResults<GXDIndexStageDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GXDIndexStageDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GXDIndexStageDomain domain = new GXDIndexStageDomain();
		if (stageDAO.get(key) != null) {
			domain = translator.translate(stageDAO.get(key));
		}
		stageDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GXDIndexStageDomain> getResults(Integer key) {
		SearchResults<GXDIndexStageDomain> results = new SearchResults<GXDIndexStageDomain>();
		results.setItem(translator.translate(stageDAO.get(key)));
		stageDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<GXDIndexStageDomain> domain, User user) {
		// process index stage results (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processGXDIndexStage/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if result is null/empty, then skip
			if (domain.get(i).getStageidKey() == null || domain.get(i).getStageidKey().isEmpty()) {
				continue;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processGXDIndexStage create");
				GXDIndexStage entity = new GXDIndexStage();
				entity.set_index_key(parentKey);
				entity.setIndexassay(termDAO.get(Integer.valueOf(domain.get(i).getIndexAssayKey())));
				entity.setStageid(termDAO.get(Integer.valueOf(domain.get(i).getStageidKey())));
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				stageDAO.persist(entity);
				modified = true;
				log.info("processGXDIndexStage/create processed: " + entity.get_index_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGXDIndexStage delete");
				GXDIndexStage entity = stageDAO.get(Integer.valueOf(domain.get(i).getIndexStageKey()));
				stageDAO.remove(entity);
				modified = true;
				log.info("processGXDIndexStage delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGXDIndexStage update");
				GXDIndexStage entity = stageDAO.get(Integer.valueOf(domain.get(i).getIndexStageKey()));
				entity.set_index_key(Integer.valueOf(domain.get(i).getIndexKey()));
				entity.setIndexassay(termDAO.get(Integer.valueOf(domain.get(i).getIndexAssayKey())));
				entity.setStageid(termDAO.get(Integer.valueOf(domain.get(i).getStageidKey())));
				entity.setModification_date(new Date());
				stageDAO.update(entity);
				modified = true;
				log.info("processGXDIndexStage/changes processed: " + domain.get(i).getIndexStageKey());	
			}
			else {
				log.info("processGXDIndexStage/no changes processed: " + domain.get(i).getIndexStageKey());
			}
		}
		
		log.info("processGXDIndexStage/processing successful");
		return modified;
	}
	    
}
