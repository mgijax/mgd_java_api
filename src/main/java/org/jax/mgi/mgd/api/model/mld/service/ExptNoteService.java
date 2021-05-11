package org.jax.mgi.mgd.api.model.mld.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.ExptNoteDAO;
import org.jax.mgi.mgd.api.model.mld.domain.ExptNoteDomain;
import org.jax.mgi.mgd.api.model.mld.entities.ExptNote;
import org.jax.mgi.mgd.api.model.mld.translator.ExptNoteTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ExptNoteService extends BaseService<ExptNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ExptNoteDAO noteDAO;

	private ExptNoteTranslator translator = new ExptNoteTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ExptNoteDomain> create(ExptNoteDomain domain, User user) {
		SearchResults<ExptNoteDomain> results = new SearchResults<ExptNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ExptNoteDomain> update(ExptNoteDomain domain, User user) {
		SearchResults<ExptNoteDomain> results = new SearchResults<ExptNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ExptNoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ExptNoteDomain domain = new ExptNoteDomain();
		if (noteDAO.get(key) != null) {
			domain = translator.translate(noteDAO.get(key));
		}
		noteDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ExptNoteDomain> getResults(Integer key) {
        SearchResults<ExptNoteDomain> results = new SearchResults<ExptNoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        noteDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ExptNoteDomain> delete(Integer key, User user) {
		SearchResults<ExptNoteDomain> results = new SearchResults<ExptNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ExptNoteDomain> search(Integer key) {

		List<ExptNoteDomain> results = new ArrayList<ExptNoteDomain>();

		String cmd = "\nselect _refs_key from mld_expt_notes where _expt_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ExptNoteDomain domain = new ExptNoteDomain();	
				domain = translator.translate(noteDAO.get(rs.getInt("_expt_key")));
				noteDAO.clear();
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
	public Boolean process(String parentKey, ExptNoteDomain domain, User user) {
		// process experiment notes (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processExptNote");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if (domain == null || domain.getNote().isEmpty()) {
				log.info("processExptNote/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processExptNote create");
			ExptNote entity = new ExptNote();
			entity.set_expt_key(Integer.valueOf(parentKey));
			entity.setNote(domain.getNote());
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			noteDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processExptNote delete");				
			ExptNote entity = noteDAO.get(Integer.valueOf(parentKey));
			noteDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processExptNote update");								
			ExptNote entity = noteDAO.get(Integer.valueOf(parentKey));				
			entity.setNote(domain.getNote());
			entity.setModification_date(new Date());
			noteDAO.update(entity);
			modified = true;
			log.info("processExptNote/changes processed: " + domain.getExptKey());
		}
		else {
			log.info("processExptNote/no changes processed: " + domain.getExptKey());
		}
		
		log.info("processExptNote/processing successful");
		return modified;
	}
	
}
