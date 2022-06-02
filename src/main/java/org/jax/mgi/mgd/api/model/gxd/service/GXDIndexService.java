package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GXDIndexDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndex;
import org.jax.mgi.mgd.api.model.gxd.translator.GXDIndexTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GXDIndexService extends BaseService<GXDIndexDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GXDIndexDAO indexDAO;
	@Inject
	private TermDAO termDAO;
//	@Inject
//	private IndexStageService stageService;
	
	private GXDIndexTranslator translator = new GXDIndexTranslator();				

	@Transactional
	public SearchResults<GXDIndexDomain> create(GXDIndexDomain domain, User user) {
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GXDIndexDomain> update(GXDIndexDomain domain, User user) {
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GXDIndexDomain> delete(Integer key, User user) {
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GXDIndexDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GXDIndexDomain domain = new GXDIndexDomain();
		if (indexDAO.get(key) != null) {
			domain = translator.translate(indexDAO.get(key));
		}
		indexDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GXDIndexDomain> getResults(Integer key) {
		SearchResults<GXDIndexDomain> results = new SearchResults<GXDIndexDomain>();
		results.setItem(translator.translate(indexDAO.get(key)));
		indexDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<GXDIndexDomain> domain, User user) {
		// process in situ results (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processGXDIndex/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if result is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
//			if (domain.get(i).getStrengthKey() == null || domain.get(i).getStrengthKey().isEmpty()) {
//				continue;
//			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				log.info("processGXDIndex create");

				GXDIndex entity = new GXDIndex();
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				
				indexDAO.persist(entity);
				
//				if (domain.get(i).getStructures() != null && !domain.get(i).getStructures().isEmpty()) {
//					modified = structureService.process(entity.get_result_key(), domain.get(i).getStructures(), user);
//				}
				
				modified = true;
				log.info("processGXDIndex/create processed: " + entity.get_index_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGXDIndex delete");
				GXDIndex entity = indexDAO.get(Integer.valueOf(domain.get(i).getIndexKey()));
				indexDAO.remove(entity);
				modified = true;
				log.info("processGXDIndex delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGXDIndex update");

				GXDIndex entity = indexDAO.get(Integer.valueOf(domain.get(i).getIndexKey()));
				entity.setModification_date(new Date());
				
//				if (domain.get(i).getStructures() != null && !domain.get(i).getStructures().isEmpty()) {
//					modified = structureService.process(Integer.valueOf(domain.get(i).getResultKey()), domain.get(i).getStructures(), user);
//				}
				
				indexDAO.update(entity);
				modified = true;
				log.info("processGXDIndex/changes processed: " + domain.get(i).getIndexKey());	
			}
			else {
				log.info("processGXDIndex/no changes processed: " + domain.get(i).getIndexKey());
			}
		}
		
		log.info("processGXDIndex/processing successful");
		return modified;
	}
	    
}
