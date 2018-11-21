package org.jax.mgi.mgd.api.model.mgi.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.NoteDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.NoteSearchForm;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class NoteService extends BaseService<NoteDomain> {

	protected static Logger log = Logger.getLogger(NoteService.class);

	@Inject
	private NoteDAO noteDAO;

	private NoteTranslator translator = new NoteTranslator();
	
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

	public List<NoteDomain> search(NoteSearchForm searchForm) {

		// list of results to be returned
		//List<NoteDomain> results = new ArrayList<NoteDomain>();

		//String cmd = "select * from mgi_note";
		//log.info(cmd);

		// request data, and parse results
		//try {
		//	ResultSet rs = sqlExecutor.executeProto(cmd);
		//	while (rs.next()) {
		//		NoteDomain noteDomain = new NoteDomain();
		//		noteDomain.setNoteKey(rs.getInt("_note_key"));
		//		noteDomain.setObjectKey(rs.getInt("_object_key"));
		//		noteDomain.setMgiTypeKey(rs.getInt("_mgitype_key"));
		//		noteDomain.setNoteTypeKey(rs.getInt("_notetype_key"));
				//noteDomain.setCreation_date(rs.getDate("creation_date"));
				//noteDomain.setModification_date(rs.getDate("modification_date"));
		//		results.add(noteDomain);
		//	}
		//	sqlExecutor.cleanup();
		//}
		//catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		//return results;
		return null;
	}
	
	@Transactional
	public static void processNote(MarkerDomain domain, NoteDomain noteDomain, NoteDAO noteDAO, String mgiTypeKey, String noteTypeKey, User user) {
		// process note by calling stored procedure (create, delete, update)
		
		String noteKey;
		String note;

		if (noteDomain == null) {
			log.info("processNote/nothing to process");
			return;
		}
		
		if (noteDomain.getNoteKey() != null || !noteDomain.getNoteKey().isEmpty()) {
			noteKey = noteDomain.getNoteKey().toString();
		}
		else {
			noteKey = "null";
		}
	
		if (!noteDomain.getNoteChunk().isEmpty()) {
			note = "'" + noteDomain.getNoteChunk().toString() + "'";
		}
		else {
			note = "null";
		}
		
		// stored procedure
		// if noteKey is null, then insert new note
		// if noteKey is not null and note is null, then delete note
		// else, update note
		// returns void
		String cmd = "select count(*) from MGI_processNote ("
				+ user.get_user_key().intValue()
				+ "," + noteKey
				+ "," + domain.getMarkerKey()
				+ "," + mgiTypeKey
				+ "," + noteTypeKey
				+ "," + note
				+ ")";

		log.info("cmd: " + cmd);
		Query query = noteDAO.createNativeQuery(cmd);
		query.getResultList();
		
		log.info("processNote/processing successful");
		return;
	}
}
