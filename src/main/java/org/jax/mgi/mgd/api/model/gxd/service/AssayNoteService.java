package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayNoteDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AssayNote;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayNoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AssayNoteService extends BaseService<AssayNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AssayNoteDAO assayNoteDAO;

	private AssayNoteTranslator translator = new AssayNoteTranslator();				

	@Transactional
	public SearchResults<AssayNoteDomain> create(AssayNoteDomain object, User user) {
		SearchResults<AssayNoteDomain> results = new SearchResults<AssayNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AssayNoteDomain> update(AssayNoteDomain object, User user) {
		SearchResults<AssayNoteDomain> results = new SearchResults<AssayNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AssayNoteDomain> delete(Integer key, User user) {
		SearchResults<AssayNoteDomain> results = new SearchResults<AssayNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AssayNoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AssayNoteDomain domain = new AssayNoteDomain();
		if (assayNoteDAO.get(key) != null) {
			domain = translator.translate(assayNoteDAO.get(key));
		}
		assayNoteDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AssayNoteDomain> getResults(Integer key) {
		SearchResults<AssayNoteDomain> results = new SearchResults<AssayNoteDomain>();
		results.setItem(translator.translate(assayNoteDAO.get(key)));
		assayNoteDAO.clear();
		return results;
    }
    
	@Transactional
	public Boolean process(Integer parentKey, AssayNoteDomain domain, User user) {
		// process assay notes (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null) {
			log.info("processAssayNote/nothing to process");
			return modified;
		}
				
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
						
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {					
			log.info("processAssayNote create");							
			if (domain.getAssayNote() != null && !domain.getAssayNote().isEmpty()) {
				AssayNote entity = new AssayNote();
				entity.set_assay_key(parentKey);
				entity.setAssayNote(domain.getAssayNote());			
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());	
				assayNoteDAO.persist(entity);
				modified = true;
				log.info("processSpecimen/create processed: " + entity.get_assay_key());									
			}
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processAssayNote delete");
			AssayNote entity = assayNoteDAO.get(Integer.valueOf(domain.getAssayNoteKey()));
			assayNoteDAO.remove(entity);
			modified = true;
			log.info("processSynonym delete successful");
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processAssayNote update");
			AssayNote entity = assayNoteDAO.get(Integer.valueOf(domain.getAssayNoteKey()));		
			entity.setAssayNote(domain.getAssayNote());			
			entity.setModification_date(new Date());
			assayNoteDAO.update(entity);
			modified = true;
			log.info("processAssayNote/changes processed: " + domain.getAssayNoteKey());	
		}
		else {
			log.info("processAssayNote/no changes processed: " + domain.getAssayNoteKey());
		}
		
		log.info("processAssayNote/processing successful");
		return modified;
	}
	
}
