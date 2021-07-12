package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.InSituResultStructureDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.TheilerStageDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure;
import org.jax.mgi.mgd.api.model.gxd.translator.InSituResultStructureTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class InSituResultStructureService extends BaseService<InSituResultStructureDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private InSituResultStructureDAO insitustructureDAO;
	@Inject
	private TermDAO termDAO;
	@Inject 
	private TheilerStageDAO theilerStageDAO;

	private InSituResultStructureTranslator translator = new InSituResultStructureTranslator();				

	@Transactional
	public SearchResults<InSituResultStructureDomain> create(InSituResultStructureDomain domain, User user) {
		SearchResults<InSituResultStructureDomain> results = new SearchResults<InSituResultStructureDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultStructureDomain> update(InSituResultStructureDomain domain, User user) {
		SearchResults<InSituResultStructureDomain> results = new SearchResults<InSituResultStructureDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultStructureDomain> delete(Integer key, User user) {
		SearchResults<InSituResultStructureDomain> results = new SearchResults<InSituResultStructureDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public InSituResultStructureDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		InSituResultStructureDomain domain = new InSituResultStructureDomain();
		if (insitustructureDAO.get(key) != null) {
			domain = translator.translate(insitustructureDAO.get(key));
		}
		insitustructureDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<InSituResultStructureDomain> getResults(Integer key) {
		SearchResults<InSituResultStructureDomain> results = new SearchResults<InSituResultStructureDomain>();
		results.setItem(translator.translate(insitustructureDAO.get(key)));
		insitustructureDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<InSituResultStructureDomain> domain, User user) {
		// process in situ result structures (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processInSituStructures/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if result is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
			if (domain.get(i).getEmapaTerm() == null || domain.get(i).getEmapaTerm().isEmpty()) {
				continue;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {			
				log.info("processInSituStructures create");

				InSituResultStructure entity = new InSituResultStructure();

				entity.set_result_key(Integer.valueOf(domain.get(i).getResultKey()));
				entity.setEmapaTerm(termDAO.get(Integer.valueOf(domain.get(i).getEmapaTermKey())));
				entity.setTheilerStage(theilerStageDAO.get(Integer.valueOf(domain.get(i).getTheilerStageKey())));				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				
				insitustructureDAO.persist(entity);				
				modified = true;
				log.info("processInSituStructures/create processed: " + entity.get_resultstructure_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processInSituStructures delete");
				InSituResultStructure entity = insitustructureDAO.get(Integer.valueOf(domain.get(i).getResultStructureKey()));
				insitustructureDAO.remove(entity);
				modified = true;
				log.info("processInSituStructures delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processInSituStructures update");

				InSituResultStructure entity = insitustructureDAO.get(Integer.valueOf(domain.get(i).getResultStructureKey()));
				
				entity.set_result_key(Integer.valueOf(domain.get(i).getResultKey()));
				entity.setEmapaTerm(termDAO.get(Integer.valueOf(domain.get(i).getEmapaTermKey())));
				entity.setTheilerStage(theilerStageDAO.get(Integer.valueOf(domain.get(i).getTheilerStageKey())));				
				entity.setModification_date(new Date());
				
				insitustructureDAO.update(entity);
				modified = true;
				log.info("processInSituStructures/changes processed: " + domain.get(i).getResultStructureKey());	
			}
			else {
				log.info("processInSituStructures/no changes processed: " + domain.get(i).getResultStructureKey());
			}
		}
		
		log.info("processInSituStructures/processing successful");
		return modified;
	}
	    
}
