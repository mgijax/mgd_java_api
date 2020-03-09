package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GelLaneStructureDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.TheilerStageDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLaneStructure;
import org.jax.mgi.mgd.api.model.gxd.translator.GelLaneStructureTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GelLaneStructureService extends BaseService<GelLaneStructureDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GelLaneStructureDAO gelLaneStructureDAO;
	@Inject
	private TermDAO termDAO;
	@Inject 
	private TheilerStageDAO theilerStageDAO;

	private GelLaneStructureTranslator translator = new GelLaneStructureTranslator();				

	@Transactional
	public SearchResults<GelLaneStructureDomain> create(GelLaneStructureDomain domain, User user) {
		SearchResults<GelLaneStructureDomain> results = new SearchResults<GelLaneStructureDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelLaneStructureDomain> update(GelLaneStructureDomain domain, User user) {
		SearchResults<GelLaneStructureDomain> results = new SearchResults<GelLaneStructureDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelLaneStructureDomain> delete(Integer key, User user) {
		SearchResults<GelLaneStructureDomain> results = new SearchResults<GelLaneStructureDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GelLaneStructureDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GelLaneStructureDomain domain = new GelLaneStructureDomain();
		if (gelLaneStructureDAO.get(key) != null) {
			domain = translator.translate(gelLaneStructureDAO.get(key));
		}
		gelLaneStructureDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GelLaneStructureDomain> getResults(Integer key) {
		SearchResults<GelLaneStructureDomain> results = new SearchResults<GelLaneStructureDomain>();
		results.setItem(translator.translate(gelLaneStructureDAO.get(key)));
		gelLaneStructureDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<GelLaneStructureDomain> domain, User user) {
		// process gxd lane structures (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processGelLaneStructures/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if result is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getEmapaTerm() == null || domain.get(i).getEmapaTerm().isEmpty()) {
					continue;
				}
				
				log.info("processGelLaneStructures create");

				GelLaneStructure entity = new GelLaneStructure();

				entity.set_gellane_key(Integer.valueOf(domain.get(i).getGelLaneKey()));
				entity.setEmapaTerm(termDAO.get(Integer.valueOf(domain.get(i).getEmapaTermKey())));
				entity.setTheilerStage(theilerStageDAO.get(Integer.valueOf(domain.get(i).getTheilerStageKey())));				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				
				gelLaneStructureDAO.persist(entity);				
				modified = true;
				log.info("processGelLaneStructures/create processed: " + entity.get_gellanestructure_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGelLaneStructures delete");
				GelLaneStructure entity = gelLaneStructureDAO.get(Integer.valueOf(domain.get(i).getGelLaneStructureKey()));
				gelLaneStructureDAO.remove(entity);
				modified = true;
				log.info("processGelLaneStructures delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGelLaneStructures update");

				GelLaneStructure entity = gelLaneStructureDAO.get(Integer.valueOf(domain.get(i).getGelLaneStructureKey()));
				
				entity.set_gellane_key(Integer.valueOf(domain.get(i).getGelLaneKey()));
				entity.setEmapaTerm(termDAO.get(Integer.valueOf(domain.get(i).getEmapaTermKey())));
				entity.setTheilerStage(theilerStageDAO.get(Integer.valueOf(domain.get(i).getTheilerStageKey())));				
				entity.setModification_date(new Date());
				
				gelLaneStructureDAO.update(entity);
				modified = true;
				log.info("processGelLaneStructures/changes processed: " + domain.get(i).getGelLaneStructureKey());	
			}
			else {
				log.info("processGelLaneStructures/no changes processed: " + domain.get(i).getGelLaneStructureKey());
			}
		}
		
		log.info("processGelLaneStructures/processing successful");
		return modified;
	}
	    
}
