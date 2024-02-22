package org.jax.mgi.mgd.api.model.mld.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.MappingNoteDAO;
import org.jax.mgi.mgd.api.model.mld.domain.MappingNoteDomain;
import org.jax.mgi.mgd.api.model.mld.entities.MappingNote;
import org.jax.mgi.mgd.api.model.mld.translator.MappingNoteTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class MappingNoteService extends BaseService<MappingNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MappingNoteDAO noteDAO;
	
	private MappingNoteTranslator translator = new MappingNoteTranslator();						

	@Transactional
	public SearchResults<MappingNoteDomain> create(MappingNoteDomain domain, User user) {
		SearchResults<MappingNoteDomain> results = new SearchResults<MappingNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MappingNoteDomain> update(MappingNoteDomain domain, User user) {
		SearchResults<MappingNoteDomain> results = new SearchResults<MappingNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public MappingNoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MappingNoteDomain domain = new MappingNoteDomain();
		if (noteDAO.get(key) != null) {
			domain = translator.translate(noteDAO.get(key));
		}
		noteDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MappingNoteDomain> getResults(Integer key) {
        SearchResults<MappingNoteDomain> results = new SearchResults<MappingNoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        noteDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<MappingNoteDomain> delete(Integer key, User user) {
		SearchResults<MappingNoteDomain> results = new SearchResults<MappingNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<MappingNoteDomain> search(Integer key) {

		List<MappingNoteDomain> results = new ArrayList<MappingNoteDomain>();

		String cmd = "\nselect _refs_key from mld_notes where _refs_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MappingNoteDomain domain = new MappingNoteDomain();	
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
	public Boolean process(String parentKey, MappingNoteDomain domain, User user) {
		// process mapping notes (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processMappingNote");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if (domain == null || domain.getNote().isEmpty()) {
				log.info("processMappingNote/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processMappingNote create");
			MappingNote entity = new MappingNote();
			entity.set_refs_key(Integer.valueOf(parentKey));
			entity.setNote(DecodeString.setDecodeToLatin9(domain.getNote()));			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			noteDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processMappingNote delete");				
			MappingNote entity = noteDAO.get(Integer.valueOf(parentKey));
			noteDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processMappingNote update");				
			MappingNote entity = noteDAO.get(Integer.valueOf(parentKey));				
			entity.setNote(DecodeString.setDecodeToLatin9(domain.getNote()));			entity.setModification_date(new Date());
			noteDAO.update(entity);
			modified = true;
			log.info("processMappingNote/changes processed: " + domain.getRefsKey());
		}
		else {
			log.info("processMappingNote/no changes processed: " + domain.getRefsKey());
		}
		
		log.info("processMappingNote/processing successful");
		return modified;
	}
	
}
