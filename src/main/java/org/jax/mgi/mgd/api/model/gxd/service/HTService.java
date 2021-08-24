package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;

import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTUserDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTVariableDomain;

// DAOs, entities and translators
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.HTExperimentDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTExperiment;

import org.jax.mgi.mgd.api.model.gxd.translator.HTExperimentTranslator;


import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;

import org.jboss.logging.Logger;

@RequestScoped
public class HTService extends BaseService<HTDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private SQLExecutor sqlExecutor = new SQLExecutor();


	@Inject
	private HTExperimentDAO htExperimentDAO;

// future services
//	@Inject
//	private HTService htService;

	private HTExperimentTranslator translator = new HTExperimentTranslator();

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
		String orderBy = "order by acc.accid ";

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
		value = searchDomain.getNotetext();			
		if (value != null && !value.isEmpty()) {	
			from = from + ", mgi_notechunk nc, mgi_note n";	
			where = where + "\nand hte._experiment_key = n._object_key ";
			where = where + "\nand nc._note_key = n._note_key ";
			where = where + "\nand n._notetype_key = 1047 ";
			where = where + "\nand nc.note ilike '" + value + "'";
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

		List<HTVariableDomain> experiment_variables = searchDomain.getExperiment_variables();
		int varJoinCount = 0;
        for (HTVariableDomain varDom : experiment_variables) {
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
				results.add(domain);		
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}



/*
*  TODO - SHELLED OUT OVER-RIDES OF BASE CLASS'S METHODS
*
*/




    @Transactional
    public SearchResults<HTDomain> getResults(Integer key) {
        SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//        results.setItem(translator.translate(assayDAO.get(key)));
        return results;
    } 



	@Transactional
	public SearchResults<HTDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
//		Assay entity = assayDAO.get(key);
//		results.setItem(translator.translate(assayDAO.get(key)));
//		assayDAO.remove(entity);
		return results;
	}  


	@Transactional
	public SearchResults<HTDomain> update(HTDomain domain, User user) {
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"
				
		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		log.info("HT update");
		

		return results;		
	}


	@Transactional
	public SearchResults<HTDomain> create(HTDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<HTDomain> results = new SearchResults<HTDomain>();
		log.info("HT create");

		return results;
	}











	
}
