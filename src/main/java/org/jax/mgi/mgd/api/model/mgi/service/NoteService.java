package org.jax.mgi.mgd.api.model.mgi.service;

import java.io.IOException;
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
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jax.mgi.mgd.api.util.RunCommand;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class NoteService extends BaseService<NoteDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private NoteDAO noteDAO;

	private NoteTranslator translator = new NoteTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<NoteDomain> create(NoteDomain object, User user) {
		SearchResults<NoteDomain> results = new SearchResults<NoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<NoteDomain> update(NoteDomain object, User user) {
		SearchResults<NoteDomain> results = new SearchResults<NoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<NoteDomain> delete(Integer key, User user) {
		SearchResults<NoteDomain> results = new SearchResults<NoteDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public NoteDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		NoteDomain domain = new NoteDomain();
		if (noteDAO.get(key) != null) {
			domain = translator.translate(noteDAO.get(key));
		}
		noteDAO.clear();
		return domain;	
	}

    @Transactional
    public SearchResults<NoteDomain> getResults(Integer key) {
        SearchResults<NoteDomain> results = new SearchResults<NoteDomain>();
        results.setItem(translator.translate(noteDAO.get(key)));
        noteDAO.clear();
        return results;
    }
    
	@Transactional	
	public List<NoteDomain> getByMarker(Integer key) {

		List<NoteDomain> results = new ArrayList<NoteDomain>();

		String cmd = "\nselect * from mgi_note_marker_view "
				+ "where _notetype_key in (1004, 1009, 1030, 1045, 1049)"
				+ "\nand _object_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
							
				NoteDomain domain = new NoteDomain();
				domain = translator.translate(noteDAO.get(rs.getInt("_note_key")));
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
	public Boolean processAll(String parentKey, List<NoteDomain> noteDomains, String mgiTypeKey, User user) {
		Boolean modified = Boolean.FALSE;
		log.info("NoteService processAll");
		if(noteDomains != null && !noteDomains.isEmpty()) {
			for (int i = 0; i < noteDomains.size(); i++) {
				NoteDomain note = noteDomains.get(i);
				String noteTypeKey = note.getNoteTypeKey();
				Boolean m = process(parentKey, note, mgiTypeKey, noteTypeKey, user);
				if (m.equals(Boolean.TRUE)) {
					modified = m;
				}
			}
		}
		return modified;
	}
		
	@Transactional
	public Boolean process(String parentKey, NoteDomain noteDomain, String mgiTypeKey, String noteTypeKey, User user) {
		// process note by calling stored procedure (create, delete, update)

		// earlier pwis may be using "String noteTypeKey" parameter
		// once these are all changed, this parameter can be removed		
		// noteTypeKey can be found in noteDomain...
		// AlleleService
		// AlleleVariantService
		// AlleleCellLineDerivationService
		// GenotypeService
		// ImageService
		// MarkerAnnotService
		// MarkerService
		// EvidenceService
		
		log.info("NoteService process");
		String noteKey = "";
		String note = "";
		
		Boolean modified = false;
			
		if (noteDomain == null) {
			log.info("processNote/no changes processed: " + parentKey);
			return modified;
		}
		
		// note key null or empty and noteChunk = "", then ignore
		if ((noteDomain.getNoteKey() == null || noteDomain.getNoteKey().isEmpty())
				&& noteDomain.getNoteChunk().equals(""))
			{
			log.info("processNote/no changes processed: " + parentKey);
			return modified;
		}		
		
		log.info("processNote/decodedToISO8859: " + noteDomain.getNoteChunk());
		note = DecodeString.setDecodeToLatin9(noteDomain.getNoteChunk());
		note = "'" + note + "'";
		
		// if processStatus is being used
		// earlier pwis are not using processStatus
		// at some point, all pwis should be changed to use processStatus
		if (noteDomain.getProcessStatus() != null) {
			if (noteDomain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				noteDomain.setNoteChunk(null);
			}
		}
		
		// if noteTypeKey is null, then set noteTypeKey = noteDomain.getNoteTypeKey()
		if (noteTypeKey == null || noteTypeKey.isEmpty()) {
			noteTypeKey = noteDomain.getNoteTypeKey();
		}
		
		// create
		if (noteDomain.getNoteKey() == null || noteDomain.getNoteKey().isEmpty())
		{
			log.info("NoteService create");
			noteKey = "null";
			modified = true;
		}
		// delete
		else if (noteDomain.getNoteChunk() == null || noteDomain.getNoteChunk().isEmpty())
		{
			log.info("NoteService delete");
			noteKey = noteDomain.getNoteKey().toString();		
			note = null;
			modified = true;
		}
		// update
		else {	
			Note entity = noteDAO.get(Integer.valueOf(noteDomain.getNoteKey()));
			if (!entity.getNoteChunk().getNote().equals(noteDomain.getNoteChunk())) {
				log.info("NoteService update");
				noteKey = noteDomain.getNoteKey().toString();
				modified = true;
			}
			if (!String.valueOf(entity.getNoteType().get_noteType_key()).equals(noteTypeKey)) {
				log.info("NoteService update");
				noteKey = noteDomain.getNoteKey().toString();
				modified = true;
			}
		}
		
		// stored procedure
		// if noteKey is null, then insert new note
		// if noteKey is not null and note is null, then delete note
		// else, update note
		// returns void
		if (modified) {
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
			modified = true;
		}
		
		log.info("processNote/changes processed: " + parentKey);
		return modified;
	}

	@Transactional		
	public Boolean processAlleleCombinations(Integer genotypeKey) throws IOException, InterruptedException {

//
//		allelecacheload/allelecombinationByGenotype.py
//		send -Kxxxx where xxxx = genotype key
//
//		this will update the genotypes Combination Type 1, 2, 3 with the proper values	
//
	
		// these swarm variables are in 'app.properties'
    	String utilitiesScript = System.getProperty("swarm.ds.alleleCombinationUtilities");
    	String server = System.getProperty("swarm.ds.dbserver");
        String db = System.getProperty("swarm.ds.dbname");
        String user = System.getProperty("swarm.ds.username");
        String pwd = System.getProperty("swarm.ds.dbpasswordfile");
                
		String runCmd = utilitiesScript;
        runCmd = runCmd + " -S" + server;
        runCmd = runCmd + " -D" + db;
        runCmd = runCmd + " -U" + user;
        runCmd = runCmd + " -P" + pwd;
        runCmd = runCmd + " -K" + String.valueOf(genotypeKey);

		Boolean modified = false;
       
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			modified = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);	
		}			

		return modified;
	}
		
}
