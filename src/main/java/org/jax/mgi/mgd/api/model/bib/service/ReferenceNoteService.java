package org.jax.mgi.mgd.api.model.bib.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceNoteDAO;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceNoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceNoteService extends BaseService<ReferenceNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ReferenceNoteDAO noteDAO;

	private ReferenceNoteTranslator translator = new ReferenceNoteTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ReferenceNoteDomain> create(ReferenceNoteDomain domain, User user) {
		SearchResults<ReferenceNoteDomain> results = new SearchResults<ReferenceNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ReferenceNoteDomain> update(ReferenceNoteDomain domain, User user) {
		SearchResults<ReferenceNoteDomain> results = new SearchResults<ReferenceNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ReferenceNoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ReferenceNoteDomain domain = new ReferenceNoteDomain();
		if (noteDAO.get(key) != null) {
			domain = translator.translate(noteDAO.get(key));
		}
		noteDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ReferenceNoteDomain> getResults(Integer key) {
        SearchResults<ReferenceNoteDomain> results = new SearchResults<ReferenceNoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        noteDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ReferenceNoteDomain> delete(Integer key, User user) {
		SearchResults<ReferenceNoteDomain> results = new SearchResults<ReferenceNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ReferenceNoteDomain> search(Integer key) {

		List<ReferenceNoteDomain> results = new ArrayList<ReferenceNoteDomain>();

		String cmd = "\nselect _refs_key from bib_notes where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ReferenceNoteDomain domain = new ReferenceNoteDomain();	
				domain = translator.translate(noteDAO.get(rs.getInt("_refs_key")));
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
	public Boolean process(String parentKey, ReferenceNoteDomain domain, User user) {
		// process reference notes (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processReferenceNote");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if (domain == null || domain.getNote().isEmpty()) {
				log.info("processReferenceNote/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {
			log.info("processReferenceNote create");
			ReferenceNote entity = new ReferenceNote();
			entity.set_refs_key(Integer.valueOf(parentKey));
			entity.setNote(domain.getNote());
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			noteDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processReferenceNote delete");				
			ReferenceNote entity = noteDAO.get(Integer.valueOf(domain.getRefsKey()));
			noteDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processReferenceNote update");								
			ReferenceNote entity = noteDAO.get(Integer.valueOf(parentKey));				
			entity.set_refs_key(Integer.valueOf(parentKey));
			entity.setNote(domain.getNote());
			entity.setModification_date(new Date());
			noteDAO.update(entity);
			modified = true;
			log.info("processReferenceNote/changes processed: " + domain.getRefsKey());
		}
		else {
			log.info("processReferenceNote/no changes processed: " + domain.getRefsKey());
		}
		
		log.info("processReferenceNote/processing successful");
		return modified;
	}
	
}
