package org.jax.mgi.mgd.api.model.mgi.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.NoteDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.util.Constants;
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

	@Transactional
	public void processNote(String parentKey, NoteDomain noteDomain, String mgiTypeKey, String noteTypeKey, User user) {
		// process note by calling stored procedure (create, delete, update)
		
		String noteKey;
		String note;

		if (noteDomain == null) {
			log.info("processNote/no changes processed: " + parentKey);
			return;
		}
		
		if (noteDomain.getProcessStatus().equals(Constants.PROCESS_CREATE))
		{
			noteKey = "null";
			note = "'" + noteDomain.getNoteChunk().toString() + "'"; 
		}
		else if (noteDomain.getProcessStatus().equals(Constants.PROCESS_DELETE))
		{
			noteKey = noteDomain.getNoteKey().toString();
			note = null;
		}
		else
		{
			noteKey = noteDomain.getNoteKey().toString();
			note = "'" + noteDomain.getNoteChunk().toString() + "'"; 
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
