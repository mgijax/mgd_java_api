package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerNoteDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerNoteTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerNoteService extends BaseService<MarkerNoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MarkerNoteDAO noteDAO;

	private MarkerNoteTranslator translator = new MarkerNoteTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerNoteDomain> create(MarkerNoteDomain domain, User user) {
		SearchResults<MarkerNoteDomain> results = new SearchResults<MarkerNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MarkerNoteDomain> update(MarkerNoteDomain domain, User user) {
		SearchResults<MarkerNoteDomain> results = new SearchResults<MarkerNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public MarkerNoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MarkerNoteDomain domain = new MarkerNoteDomain();
		if (noteDAO.get(key) != null) {
			domain = translator.translate(noteDAO.get(key));
		}
		noteDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MarkerNoteDomain> getResults(Integer key) {
        SearchResults<MarkerNoteDomain> results = new SearchResults<MarkerNoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        noteDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<MarkerNoteDomain> delete(Integer key, User user) {
		SearchResults<MarkerNoteDomain> results = new SearchResults<MarkerNoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<MarkerNoteDomain> search(Integer key) {

		List<MarkerNoteDomain> results = new ArrayList<MarkerNoteDomain>();

		String cmd = "\nselect _marker_key from mrk_notes where _marker_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerNoteDomain domain = new MarkerNoteDomain();	
				domain = translator.translate(noteDAO.get(rs.getInt("_marker_key")));
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
	public Boolean process(String parentKey, MarkerNoteDomain domain, User user) {
		// process marker notes (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processMarkerNote");
		
		if (domain == null || domain.getNote().isEmpty()) {
			log.info("processMarkerNote/nothing to process");
			return modified;
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processMarkerNote create");
			MarkerNote entity = noteDAO.get(Integer.valueOf(domain.getMarkerKey()));
			entity.setNote(domain.getNote());
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			noteDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processMarkerNote delete");				
			MarkerNote entity = noteDAO.get(Integer.valueOf(domain.getMarkerKey()));
			noteDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processMarkerNote update");								
			MarkerNote entity = noteDAO.get(Integer.valueOf(domain.getMarkerKey()));				
			entity.setNote(domain.getNote());
			entity.setModification_date(new Date());
			noteDAO.update(entity);
			modified = true;
			log.info("processMarkerNote/changes processed: " + domain.getMarkerKey());
		}
		else {
			log.info("processMarkerNote/no changes processed: " + domain.getMarkerKey());
		}
		
		log.info("processMarkerNote/processing successful");
		return modified;
	}
	
}
