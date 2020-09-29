package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeNoteDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeNoteDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNote;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeNoteTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeNoteService extends BaseService<ProbeNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeNoteDAO noteDAO;

	private ProbeNoteTranslator translator = new ProbeNoteTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeNoteDomain> create(ProbeNoteDomain domain, User user) {
		SearchResults<ProbeNoteDomain> results = new SearchResults<ProbeNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeNoteDomain> update(ProbeNoteDomain domain, User user) {
		SearchResults<ProbeNoteDomain> results = new SearchResults<ProbeNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ProbeNoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeNoteDomain domain = new ProbeNoteDomain();
		if (noteDAO.get(key) != null) {
			domain = translator.translate(noteDAO.get(key));
		}
		noteDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ProbeNoteDomain> getResults(Integer key) {
        SearchResults<ProbeNoteDomain> results = new SearchResults<ProbeNoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        noteDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ProbeNoteDomain> delete(Integer key, User user) {
		SearchResults<ProbeNoteDomain> results = new SearchResults<ProbeNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ProbeNoteDomain> search(Integer key) {

		List<ProbeNoteDomain> results = new ArrayList<ProbeNoteDomain>();

		String cmd = "\nselect _note_key from prb_notes where _probe_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ProbeNoteDomain domain = new ProbeNoteDomain();	
				domain = translator.translate(noteDAO.get(rs.getInt("_note_key")));
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
	public Boolean process(String parentKey, ProbeNoteDomain domain, User user) {
		// process probe notes (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processProbeNote");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if (domain == null || domain.getNote().isEmpty()) {
				log.info("processProbeNote/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processProbeNote create");
			ProbeNote entity = new ProbeNote();
			entity.set_probe_key(Integer.valueOf(domain.getProbeKey()));
			entity.setNote(domain.getNote());
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			noteDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processProbeNote delete");				
			ProbeNote entity = noteDAO.get(Integer.valueOf(domain.getNoteKey()));
			noteDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processProbeNote update");								
			ProbeNote entity = noteDAO.get(Integer.valueOf(domain.getNoteKey()));				
			entity.setNote(domain.getNote());
			entity.setModification_date(new Date());
			noteDAO.update(entity);
			modified = true;
			log.info("processProbeNote/changes processed: " + domain.getNoteKey());
		}
		else {
			log.info("processProbeNote/no changes processed: " + domain.getNoteKey());
		}
		
		log.info("processProbeNote/processing successful");
		return modified;
	}
	
}
