package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.HTExperimentDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTExperimentVariableDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;
import org.jax.mgi.mgd.api.model.gxd.translator.HTExperimentTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIPropertyDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIPropertyService;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class HTExperimentService extends BaseService<HTDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private SQLExecutor sqlExecutor = new SQLExecutor();
	private HTExperimentTranslator translator = new HTExperimentTranslator();

	@Inject
	private HTExperimentDAO htExperimentDAO;
	@Inject
	private HTSampleService htSampleService;
	@Inject
	private HTExperimentVariableService hTExperimentVariableService;
	@Inject
	private NoteService noteService;
	@Inject
	private MGIPropertyService mgiPropertyService;
	@Inject
	private TermDAO termDAO;

	@Transactional
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}	

	@Transactional
	public SearchResults<HTDomain> update(HTDomain domain, User user) {
				
		log.info("processHTExperiment/update");

		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		HTExperiment entity = htExperimentDAO.get(domain.get_experiment_key());
		
		// name
		if (domain.getName() == null || domain.getDescription().isEmpty()) {
			entity.setName(null);
		}
		else {
			entity.setName(domain.getName());
		}
		
		// description
		if (domain.getDescription() == null || domain.getDescription().isEmpty()) {
			entity.setDescription(null);
		}
		else {
			entity.setDescription(domain.getDescription());
		}

		// set initial curation user/dates when samples are created
		if (domain.getCreatingSamples() == 1) {
			entity.setInitial_curated_date(new Date());
			entity.setInitialcuratedBy(user);
		}
		
		// last curation user/date set when samples created / updated / deleted
		if (domain.getDeletingSamples() == 1 || domain.getModifyingSamples() == 1 || domain.getCreatingSamples() == 1) {
			entity.setLast_curated_date(new Date());
			entity.setLastcuratedBy(user);
		}

		// evaluation user/date set when evaluation state has changed
		if (entity.getEvaluationState().get_term_key() != domain.get_evaluationstate_key()){
			entity.setEvaluated_date(new Date());
			entity.setEvaluatedBy(user);
		}
		
		// curation state
		entity.setCurationState(termDAO.get(Integer.valueOf(domain.get_curationstate_key())));	
		// evaluation state
		entity.setEvaluationState(termDAO.get(Integer.valueOf(domain.get_evaluationstate_key())));	
		// experiment type
		entity.setExperimentType(termDAO.get(Integer.valueOf(domain.get_experimenttype_key())));	
		// study type
		entity.setStudyType(termDAO.get(Integer.valueOf(domain.get_studytype_key())));	

		// experiment note 
		if (domain.getNotetext() != null){
			NoteDomain noteDomain = new NoteDomain();
			noteDomain.setNoteChunk(domain.getNotetext());
			noteDomain.setNoteTypeKey("1047");
			noteDomain.setNoteKey(domain.get_note_key());
			noteService.process(String.valueOf(entity.get_experiment_key()), noteDomain, "42", user);
		}

		// experiment variables
		if (domain.getExperiment_variables() != null) {
			hTExperimentVariableService.process(domain.get_experiment_key(), domain.getExperiment_variables(), user);
		}

		// pubmed IDs
		if (domain.getDeletingPubmedIds() == 1) { //delete all associated

			List<MGIPropertyDomain> removedPubmedIds = new ArrayList<MGIPropertyDomain>();

			if (domain.getPubmed_property_keys() != null) {
				for (int i = 0; i < domain.getPubmed_property_keys().size(); i++) {
					MGIPropertyDomain remPropertyDomain = new MGIPropertyDomain();
					remPropertyDomain.setProcessStatus(Constants.PROCESS_DELETE);
					remPropertyDomain.setPropertyKey(domain.getPubmed_property_keys().get(i));
					removedPubmedIds.add(remPropertyDomain);
				}
				mgiPropertyService.process(removedPubmedIds, user);
			}

		}
		
		if (domain.getNewPubmedIds() != null) {
			// This value comes through as a single white-space separated string, so
			// it must be broken down to loop over individual string IDs
			List<MGIPropertyDomain> newPubmedIds = new ArrayList<MGIPropertyDomain>();
			String[] newPubmedIdsArray = domain.getNewPubmedIds().trim().split("\\s++");
			for( int i = 0; i < newPubmedIdsArray.length; i++)
			{
    			String thisID = newPubmedIdsArray[i];
				MGIPropertyDomain propertyDomain = new MGIPropertyDomain();
				propertyDomain.setProcessStatus(Constants.PROCESS_CREATE);
				propertyDomain.setPropertyTermKey("20475430");
				propertyDomain.setPropertyTypeKey("1002");
				propertyDomain.setObjectKey(Integer.toString(domain.get_experiment_key()));
				propertyDomain.setMgiTypeKey("42");
				propertyDomain.setValue(thisID);
				propertyDomain.setSequenceNum(String.valueOf(i + 1));
				newPubmedIds.add(propertyDomain);
			}

			mgiPropertyService.process(newPubmedIds, user);
		}

		// process ht samples
		if (domain.getSamples() != null) {
			htSampleService.process(domain.get_experiment_key(), domain.getSamples(), user);
		}
		
		// persist entity
		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		htExperimentDAO.update(entity);

		// return entity translated to domain
		results.setItem(translator.translate(entity));
		log.info("processHTExperiment/update/returning results");
		return results;		
	}

	@Transactional
	public SearchResults<HTDomain> delete(Integer key, User user) {
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public HTDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		HTDomain domain = new HTDomain();
		HTExperiment entity = htExperimentDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity); 
		}
		return domain;
	}

	@Transactional	
	public SearchResults<HTDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		String cmd = "select count(*) as objectCount from gxd_htexperiment";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
    @Transactional
    public SearchResults<HTDomain> getResults(Integer key) {
        SearchResults<HTDomain> results = new SearchResults<HTDomain>();
        results.setItem(translator.translate(htExperimentDAO.get(key)));
        return results;
    } 

	@Transactional
	public List<SlimHTDomain> search(HTDomain searchDomain) {

		List<SlimHTDomain> results = new ArrayList<SlimHTDomain>();
		
		// used to hold searchDomain values passed in web request
		String value;

		// Building SQL command : select + from + where + orderBy
		// Other FROM/WHERE clauses will be added, determined by query parameters
		String cmd = "";
		String select = "select acc.accid, hte._experiment_key, hte._curationstate_key ";
		String from = "from gxd_htexperiment hte, acc_accession acc ";
		String where = "where hte._experiment_key = acc._object_key " 
				+ "\nand acc._mgitype_key = 42"
				+ "\nand acc.preferred = 1 ";
		String orderBy = "order by acc.prefixpart, acc.numericPart ";

		// primary accession id 
		value = searchDomain.getPrimaryid();			
		if (value != null && !value.isEmpty()) {	
			from = from + ", acc_accession acc1 ";	
			where = where + "\nand hte._experiment_key = acc1._object_key ";
			where = where + "\nand acc1._mgitype_key = 42 ";
			where = where + "\nand acc1._logicaldb_key = 189 ";
			where = where + "\nand acc1.accID ilike '" + value + "'";
		}

		// secondary accession id 
		value = searchDomain.getSecondaryid();			
		if (value != null && !value.isEmpty()) {
			from = from + ", acc_accession acc2 ";	
			where = where + "\nand hte._experiment_key = acc2._object_key ";
			where = where + "\nand acc2._mgitype_key = 42 ";
			where = where + "\nand acc2._logicaldb_key = 190 ";
			where = where + "\nand acc2.accID ilike '" + value + "'";
		}

		// name 
		value = searchDomain.getName();			
		if (value != null && !value.isEmpty()) {	
			where = where + "\nand hte.name ilike '" + value + "'";
		}
		
		// description 
		value = searchDomain.getDescription();			
		if (value != null && !value.isEmpty()) {	
			where = where + "\nand hte.description ilike '" + value + "'";
		}

		// exp note 
		if (searchDomain.getNotetext() != null && !searchDomain.getNotetext().isEmpty()) {
			value = searchDomain.getNotetext().replaceAll("'", "''");		
			if (value != null && !value.isEmpty()) {	
				from = from + ", mgi_note n";	
				where = where + "\nand hte._experiment_key = n._object_key ";
				where = where + "\nand n._notetype_key = 1047 ";
				where = where + "\nand n.note ilike '" + value + "'";
			}
		}

		/*
		// TYPES AND STATES;  Web passes these as ints (for some reason)
		*/

		// evaluation state 
		Integer ivalue;
		ivalue = searchDomain.get_evaluationstate_key();			
		if (ivalue != null && ivalue.intValue() != 0) {	
			where = where + "\nand hte._evaluationstate_key = '" + ivalue + "'";
		}

		// experiment type 
		ivalue = searchDomain.get_experimenttype_key();			
		if (ivalue != null && ivalue.intValue() != 0) {	
			where = where + "\nand hte._experimenttype_key = '" + ivalue + "'";
		}

		// study type 
		ivalue = searchDomain.get_studytype_key();			
		if (ivalue != null && ivalue.intValue() != 0) {	
			where = where + "\nand hte._studytype_key = '" + ivalue + "'";
		}

		// curationstate state 
		ivalue = searchDomain.get_curationstate_key();			
		if (ivalue != null && ivalue.intValue() != 0) {	
			where = where + "\nand hte._curationstate_key = '" + ivalue + "'";
		}


		/* 
		// DATES
		*/

		// creation date 
		value = searchDomain.getCreation_date();			
		if (value != null && !value.isEmpty()) {	
			String dateWhereClause = DateSQLQuery.createDateWhereClause( "hte.creation_date", value );
			where = where + dateWhereClause;
		}
		// release date 
		value = searchDomain.getRelease_date();			
		if (value != null && !value.isEmpty()) {	
			String dateWhereClause = DateSQLQuery.createDateWhereClause( "hte.release_date", value );
			where = where + dateWhereClause;
		}
		// last update  
		value = searchDomain.getLastupdate_date();			
		if (value != null && !value.isEmpty()) {	
			String dateWhereClause = DateSQLQuery.createDateWhereClause( "hte.lastupdate_date", value );
			where = where + dateWhereClause;
		}
		// evaluated date  
		value = searchDomain.getEvaluated_date();			
		if (value != null && !value.isEmpty()) {	
			String dateWhereClause = DateSQLQuery.createDateWhereClause( "hte.evaluated_date", value );
			where = where + dateWhereClause;
		}
		// initial curated date
		value = searchDomain.getInitial_curated_date();			
		if (value != null && !value.isEmpty()) {	
			String dateWhereClause = DateSQLQuery.createDateWhereClause( "hte.initial_curated_date", value );
			where = where + dateWhereClause;
		}
		// last curated date
		value = searchDomain.getLast_curated_date();			
		if (value != null && !value.isEmpty()) {	
			String dateWhereClause = DateSQLQuery.createDateWhereClause( "hte.last_curated_date", value );
			where = where + dateWhereClause;
		}


		/*
		// USERS
		*/

		// evaluated by
		HTUserDomain evalUser = searchDomain.getEvaluatedby_object();
		if (evalUser != null) {	
			value = evalUser.getLogin();
			if (!value.isEmpty()) {
				from = from + ", mgi_user u1 ";
				where = where + "\nand hte._evaluatedby_key = u1._user_key";
				where = where + "\nand u1.login ilike '" + value + "'";
			}
		}
		// Initial User 
		HTUserDomain initUser = searchDomain.getInitialcuratedby_object();
		if (initUser != null ) {	
			value = initUser.getLogin();			
			if (!value.isEmpty()) {
				from = from + ", mgi_user u2 ";
				where = where + "\nand hte._initialcuratedby_key = u2._user_key";
				where = where + "\nand u2.login ilike '" + value + "'";
			}
		}
		// Last User 
		HTUserDomain lastUser = searchDomain.getLastcuratedby_object();
		if (lastUser != null ) {	
			value = lastUser.getLogin();	
			if (!value.isEmpty()) {
				from = from + ", mgi_user u3 ";
				where = where + "\nand hte._lastcuratedby_key = u3._user_key";
				where = where + "\nand u3.login ilike '" + value + "'";
			}
		}

		/*
		// EXPERIMENT VARIABLES
		*/

		List<HTExperimentVariableDomain> experiment_variables = searchDomain.getExperiment_variables();
		int varJoinCount = 0;
        for (HTExperimentVariableDomain varDom : experiment_variables) {
            //TODO --if for false checks
            varJoinCount++;
        	from = from + ", gxd_htexperimentvariable htev" + Integer.toString(varJoinCount);
        	where = where + "\nand hte._experiment_key = htev" + Integer.toString(varJoinCount) + "._experiment_key ";
			where = where + "\nand htev" + Integer.toString(varJoinCount) + "._term_key = " + varDom.getTermKey();
        }


		// log for easy copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimHTDomain domain = new SlimHTDomain();

				domain.set_experiment_key(rs.getString("_experiment_key"));
				domain.setPrimaryid(rs.getString("accid"));
				domain.set_curationstate_key(rs.getString("_curationstate_key"));
//				if (rs.getString("_curationstate_key").equals("20475421")){
//					domain.setPrimaryid(rs.getString("accid") + "*");
//				}
				results.add(domain);		
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	
}
