package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.InSituResultDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.PatternDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.StrengthDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResult;
import org.jax.mgi.mgd.api.model.gxd.translator.InSituResultTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class InSituResultService extends BaseService<InSituResultDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private InSituResultDAO resultDAO;
	@Inject
	private StrengthDAO strengthDAO;
	@Inject 
	private PatternDAO patternDAO;
	@Inject
	private InSituResultStructureService structureService;
	@Inject 
	private InSituResultImageService imagePaneService;
	
	private InSituResultTranslator translator = new InSituResultTranslator();				

	@Transactional
	public SearchResults<InSituResultDomain> create(InSituResultDomain domain, User user) {
		SearchResults<InSituResultDomain> results = new SearchResults<InSituResultDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultDomain> update(InSituResultDomain domain, User user) {
		SearchResults<InSituResultDomain> results = new SearchResults<InSituResultDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultDomain> delete(Integer key, User user) {
		SearchResults<InSituResultDomain> results = new SearchResults<InSituResultDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public InSituResultDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		InSituResultDomain domain = new InSituResultDomain();
		if (resultDAO.get(key) != null) {
			domain = translator.translate(resultDAO.get(key));
		}
		resultDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<InSituResultDomain> getResults(Integer key) {
		SearchResults<InSituResultDomain> results = new SearchResults<InSituResultDomain>();
		results.setItem(translator.translate(resultDAO.get(key)));
		resultDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<InSituResultDomain> domain, User user) {
		// process in situ results (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processInSituResults/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if result is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getStrengthKey() == null || domain.get(i).getStrengthKey().isEmpty()) {
					continue;
				}
				
				log.info("processInSituResults create");

				InSituResult entity = new InSituResult();
				
				entity.set_specimen_key(parentKey);
				entity.setStrength(strengthDAO.get(Integer.valueOf(domain.get(i).getStrengthKey())));
				entity.setPattern(patternDAO.get(Integer.valueOf(domain.get(i).getPatternKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				
				if (domain.get(i).getResultNote() != null && !domain.get(i).getResultNote().isEmpty()) {
					entity.setResultNote(domain.get(i).getResultNote());
				}
				else {
					entity.setResultNote(null);
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				
				resultDAO.persist(entity);
				
				if (domain.get(i).getStructures() != null && !domain.get(i).getStructures().isEmpty()) {
					modified = structureService.process(entity.get_result_key(), domain.get(i).getStructures(), user);
				}

//				if (domain.get(i).getImagePanes() != null && !domain.get(i).getImagePanes().isEmpty()) {
//					modified = imagePaneService.process(entity.get_result_key(), domain.get(i).getImagePanes(), user);
//				}
				
				modified = true;
				log.info("processInSituResults/create processed: " + entity.get_result_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processInSituResults delete");
				InSituResult entity = resultDAO.get(Integer.valueOf(domain.get(i).getResultKey()));
				resultDAO.remove(entity);
				modified = true;
				log.info("processInSituResults delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processInSituResults update");

				InSituResult entity = resultDAO.get(Integer.valueOf(domain.get(i).getResultKey()));
				
				entity.set_specimen_key(parentKey);
				entity.setStrength(strengthDAO.get(Integer.valueOf(domain.get(i).getStrengthKey())));
				entity.setPattern(patternDAO.get(Integer.valueOf(domain.get(i).getPatternKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setResultNote(domain.get(i).getResultNote());
				entity.setModification_date(new Date());
				
				if (domain.get(i).getStructures() != null && !domain.get(i).getStructures().isEmpty()) {
					modified = structureService.process(Integer.valueOf(domain.get(i).getResultKey()), domain.get(i).getStructures(), user);
				}

//				if (domain.get(i).getImagePanes() != null && !domain.get(i).getImagePanes().isEmpty()) {
//					modified = imagePaneService.process(Integer.valueOf(domain.get(i).getResultKey()), domain.get(i).getImagePanes(), user);
//				}
				
				resultDAO.update(entity);
				modified = true;
				log.info("processInSituResults/changes processed: " + domain.get(i).getResultKey());	
			}
			else {
				log.info("processInSituResults/no changes processed: " + domain.get(i).getResultKey());
			}
		}
		
		log.info("processInSituResults/processing successful");
		return modified;
	}
	    
}
