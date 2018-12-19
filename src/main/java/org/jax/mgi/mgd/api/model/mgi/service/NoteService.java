package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.NoteDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class NoteService extends BaseService<NoteDomain> {

	protected static Logger log = Logger.getLogger(NoteService.class);

	@Inject
	private NoteDAO noteDAO;

	private NoteTranslator translator = new NoteTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<NoteDomain> create(NoteDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<NoteDomain> update(NoteDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public NoteDomain get(Integer key) {
		return translator.translate(noteDAO.get(key));
	}

    @Transactional
    public SearchResults<NoteDomain> getResults(Integer key) {
        SearchResults<NoteDomain> results = new SearchResults<NoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<NoteDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<NoteDomain> marker(Integer key) {

		List<NoteDomain> results = new ArrayList<NoteDomain>();

		String cmd = "\nselect * from mgi_note_marker_view "
				+ "where _notetype_key in (1004, 1009, 1030, 1045, 1049)"
				+ "\nand _object_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
							
				NoteDomain domain = new NoteDomain();

				//domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setNoteKey(rs.getString("_note_key"));
				domain.setObjectKey(rs.getString("_object_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setMgiType(rs.getString("mgiType"));
				domain.setNoteTypeKey(rs.getString("_notetype_key"));
				domain.setNoteType(rs.getString("notetype"));
				domain.setNoteChunk(rs.getString("note"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdby"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedby"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
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
	public void process(String parentKey, NoteDomain noteDomain, String mgiTypeKey, String noteTypeKey, User user) {
		// process note by calling stored procedure (create, delete, update)
		
		String noteKey;
		String note;

		if (noteDomain == null) {
			log.info("processNote/no changes processed: " + parentKey);
			return;
		}
		
		// create
		if (noteDomain.getNoteKey() == null || noteDomain.getNoteKey().isEmpty())
		{
			noteKey = "null";
			note = "'" + noteDomain.getNoteChunk().replaceAll("'",  "''") + "'"; 
		}
		// delete
		else if (noteDomain.getNoteChunk() == null || noteDomain.getNoteChunk().isEmpty())
		{
			noteKey = noteDomain.getNoteKey().toString();		
			note = null;	
		}
		// update
		else {
			noteKey = noteDomain.getNoteKey().toString();
			note = "'" + noteDomain.getNoteChunk().replaceAll("'",  "''") + "'"; 
		}
				
		// stored procedure
		// if noteKey is null, then insert new note
		// if noteKey is not null and note is null, then delete note
		// else, update note
		// returns void
		String cmd = "select count(*) from MGI_processNote ("
				+ user.get_user_key().intValue()
				+ "," + noteKey
				+ "," + parentKey
				+ "," + mgiTypeKey
				+ "," + noteTypeKey
				+ "," + note
				+ ")";

		log.info("cmd: " + cmd);
		Query query = noteDAO.createNativeQuery(cmd);
		query.getResultList();
		
		log.info("processNote/changes processed: " + parentKey);
		return;
	}
	
}
