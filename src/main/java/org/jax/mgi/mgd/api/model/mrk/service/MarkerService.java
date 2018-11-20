package org.jax.mgi.mgd.api.model.mrk.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.EventDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.EventReasonDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerHistoryDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerStatusDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerTypeDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIResultDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.MarkerWithdrawal;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerService extends BaseService<MarkerDomain> {

	protected Logger log = Logger.getLogger(MarkerService.class);

	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private MarkerHistoryDAO historyDAO;
	@Inject
	private MGISynonymDAO synonymDAO;
	
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private MarkerStatusDAO markerStatusDAO;
	@Inject
	private MarkerTypeDAO markerTypeDAO;

	@Inject
	private EventDAO eventDAO;
	@Inject
	private EventReasonDAO eventReasonDAO;
	@Inject
	private ReferenceDAO referenceDAO;

	private MarkerTranslator translator = new MarkerTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = new Marker();
		
		// set entity attributes
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		entity.setChromosome(domain.getChromosome());
		// end set entity fields
		
		// business rules
		
		// for cmOffset
		if (domain.getChromosome().equals("UN")) {
			entity.setCmOffset(-999.0);
		}
		else {
			entity.setCmOffset(-1.0);
		}
		
		// end business logic
		
		// convert String-to-Integer
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		entity.setMarkerStatus(markerStatusDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));
		entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));
		// end String-to-Integer conversion
		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		// end creation/modification
		
		// execute persist/insert/send to database
		markerDAO.persist(entity);

		// create marker history assignment
		// create 1 marker history row to track the initial marker assignment
		
		// default reference is J:23000 (22864)
		String refKey;
		if (domain.getHistory() == null) {
			refKey = "22864";
		}
		else {
			refKey = domain.getHistory().get(0).getRefKey().toString();
		}
		
		// event = assigned (1)
		// event reason = Not Specified (-1)
		String cmd = "select count(*) from MRK_insertHistory ("
				+ user.get_user_key().intValue()
				+ "," + entity.get_marker_key()
				+ "," + entity.get_marker_key()
				+ "," + refKey
				+ ",1,-1"
				+ ",'" + entity.getName() + "'"
				+ ")";

		log.info("cmd: " + cmd);
		Query query = markerDAO.createNativeQuery(cmd);
		query.getResultList();
		
		// create marker synonyms, if provided
		
		// return entity translated to domain
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerDomain> update(MarkerDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = markerDAO.get(Integer.valueOf(domain.getMarkerKey()));
		Boolean modified = false;
				
		if (!entity.getSymbol().equals(domain.getSymbol())) {
			entity.setSymbol(domain.getSymbol());
			modified = true;
		}
		
		if (!entity.getName().equals(domain.getName())) {
			entity.setName(domain.getName());
			modified = true;
		}
		
		if (!entity.getChromosome().equals(domain.getChromosome())) {
			
			entity.setChromosome(domain.getChromosome());
			
			if (domain.getChromosome().equals("UN")) {
				entity.setCmOffset(-999.0);
			}
			
			modified = true;
		}
		
		if (entity.getCytogeneticOffset() == null || entity.getCytogeneticOffset().isEmpty()) {
			if (!(domain.getCytogeneticOffset() == null || domain.getCytogeneticOffset().isEmpty())) {
				entity.setCytogeneticOffset(domain.getCytogeneticOffset());
				modified = true;	
			}
		}
		else if (domain.getCytogeneticOffset() == null || domain.getCytogeneticOffset().isEmpty()) {
			entity.setCytogeneticOffset(domain.getCytogeneticOffset());
			modified = true;
		}
		else if (!entity.getCytogeneticOffset().equals(domain.getCytogeneticOffset())) {
			entity.setCytogeneticOffset(domain.getCytogeneticOffset());
			modified = true;
		}
	
		// cannot change the status to "withdrawn"/2
		if (!entity.getMarkerStatus().getStatus().equals(domain.getMarkerStatus())) {
			if (domain.getMarkerStatusKey().equals("2")) {
				results.setError("Failed : Marker Status error",  "Cannot change Marker Status to 'withdrawn'", Constants.HTTP_SERVER_ERROR);
				return results;
			}
			else {
				entity.setMarkerStatus(markerStatusDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));
				modified = true;
			}
		}
		
		if (!entity.getMarkerType().getName().equals(domain.getMarkerType())) {
			entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));
			modified = true;
		}
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			markerDAO.update(entity);
			log.info("processMarker/changes processed: " + domain.getMarkerKey());
		}
		else {
			log.info("processMarker/no changes processed: " + domain.getMarkerKey());
		}
		
		// process all marker notes
		// using domain because processNote() is using stored procedure
		processNote(domain, domain.getEditorNote(), "2", "1004", user);
		processNote(domain, domain.getSequenceNote(), "2", "1009", user);
		processNote(domain, domain.getRevisionNote(), "2", "1030", user);
		processNote(domain, domain.getStrainNote(), "2", "1035", user);
		processNote(domain, domain.getLocationNote(), "2", "1049", user);

		// process marker history
		processHistory(domain.getMarkerKey(), domain.getHistory(), user);
		
		// add markerSynonymDAO
		// add markerAccessionDAO
		
		// return entity translated to domain
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public MarkerDomain get(Integer key) {
		return translator.translate(markerDAO.get(key));
	}

	@Transactional
	public SearchResults<MarkerDomain> getResults(Integer key) {
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results.setItem(translator.translate(markerDAO.get(key)));
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = markerDAO.get(key);
		markerDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public void processHistory(String parentKey, List<MarkerHistoryDomain> domain, User user) {
		// create marker history associations
		
		if (domain == null || domain.isEmpty()) {
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getAssocKey() == null 
					|| domain.get(i).getAssocKey().isEmpty()) {
				cmd = "select count(*) from MRK_insertHistory ("
							+ user.get_user_key().intValue()
							+ "," + parentKey
							+ "," + domain.get(i).getMarkerHistorySymbolKey()
							+ "," + domain.get(i).getRefKey()
							+ "," + domain.get(i).getMarkerEventKey()
							+ "," + domain.get(i).getMarkerEventReasonKey()
							+ ",'" + domain.get(i).getMarkerHistoryName() + "'"
							+ ")";
				log.info("cmd: " + cmd);
				Query query = markerDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getMarkerHistorySymbolKey().isEmpty()
					&& domain.get(i).getMarkerHistoryName().isEmpty()) {
				// process delete
				
				MarkerHistory entity = historyDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				historyDAO.remove(entity);
				
				// reset the sequence numbers
				cmd = "select count(*) from MGI_resetSequenceNum('MRK_History'" 
						+ "," + parentKey
						+ "," + user.get_user_key().intValue()
						+ ")";
				Query query = markerDAO.createNativeQuery(cmd);
				query.getResultList();
				
				log.info("processHistory delete successful");
			}
			else {
				// process update

				Boolean modified = false;
				MarkerHistory entity = historyDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));

				if (!String.valueOf(entity.get_marker_key()).equals(domain.get(i).getMarkerKey())) {
					entity.set_marker_key(Integer.valueOf(domain.get(i).getMarkerKey()));
					modified = true;
				}
				
				if (!entity.getMarkerEvent().equals(eventDAO.get(Integer.valueOf(domain.get(i).getMarkerEventKey())))) {
					entity.setMarkerEvent(eventDAO.get((Integer.valueOf(domain.get(i).getMarkerEventKey()))));
					modified = true;
				}
				
				if (!entity.getMarkerEventReason().equals(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())))) {
					entity.setMarkerEventReason(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())));
					modified = true;
				}
				
				// reference can be null
				if (!(entity.getReference() == null && domain.get(i).getRefKey() == null)) {
					if (!entity.getReference().get_refs_key().equals(Integer.valueOf(domain.get(i).getRefKey()))) {
						entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
						modified = true;
					}
				}
				else if (domain.get(i).getRefKey() == null) {
					entity.setReference(null);
					modified = true;
				}
				else {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
					modified = true;
				}
				
				// name can be null
				if (!entity.getName().equals(domain.get(i).getMarkerHistoryName())) {
					if (domain.get(i).getMarkerHistoryName() == null 
						&& domain.get(i).getMarkerHistoryName().isEmpty()) {
						entity.setName(null);
					}
					else {
						entity.setName(domain.get(i).getMarkerHistoryName());
					}
					modified = true;
				}
					
				if (!entity.getEvent_date().toString().equals(domain.get(i).getEvent_date())) {
					try {
						// convert String to Date
						entity.setEvent_date(new SimpleDateFormat("yyyy-MM-dd").parse(domain.get(i).getEvent_date()));
						modified = true;
					}
					catch (ParseException  e) {
						return;
					}
				}
				
				if (modified == true) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					historyDAO.update(entity);
					log.info("processHistory/changes processed: " + domain.get(i).getAssocKey());
				}
				else {
					log.info("processHistory/no changes processed: " + domain.get(i).getAssocKey());
				}
			}
		}
		
		log.info("processHistory/ran successfully");
		return;
	}

	@Transactional
	public void processSynonym(String parentKey, List<MGISynonymDomain> domain, User user) {
		// create marker synonym associations
		
		if (domain == null || domain.isEmpty()) {
			return;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getSynonymKey() == null 
					|| domain.get(i).getSynonymKey().isEmpty()) {
				//cmd = "select count(*) from MRK_insertHistory ("
				//			+ user.get_user_key().intValue()
				//			+ "," + parentKey
				//			+ "," + domain.get(i).getMarkerHistorySymbolKey()
				//			+ "," + domain.get(i).getRefKey()
				//			+ "," + domain.get(i).getMarkerEventKey()
				//			+ "," + domain.get(i).getMarkerEventReasonKey()
				//			+ ",'" + domain.get(i).getMarkerHistoryName() + "'"
				//			+ ")";
				log.info("cmd: " + cmd);
				Query query = markerDAO.createNativeQuery(cmd);
				query.getResultList();
			}
			else if (domain.get(i).getSynonym().isEmpty()) {
				// process delete
				
				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));
				synonymDAO.remove(entity);
				log.info("processSynonym delete successful");
			}
			else {
				// process update

				Boolean modified = false;
				MGISynonym entity = synonymDAO.get(Integer.valueOf(domain.get(i).getSynonymKey()));

				//if (!String.valueOf(entity.get_marker_key()).equals(domain.get(i).getMarkerKey())) {
				//	entity.set_marker_key(Integer.valueOf(domain.get(i).getMarkerKey()));
				//	modified = true;
				//}
				
				//if (!entity.getMarkerEvent().equals(eventDAO.get(Integer.valueOf(domain.get(i).getMarkerEventKey())))) {
				//	entity.setMarkerEvent(eventDAO.get((Integer.valueOf(domain.get(i).getMarkerEventKey()))));
				//	modified = true;
				//}
				
				//if (!entity.getMarkerEventReason().equals(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())))) {
				//	entity.setMarkerEventReason(eventReasonDAO.get(Integer.valueOf(domain.get(i).getMarkerEventReasonKey())));
				//	modified = true;
				//}
				
				// reference can be null
				if (!(entity.getReference() == null && domain.get(i).getRefKey() == null)) {
					if (!entity.getReference().get_refs_key().equals(Integer.valueOf(domain.get(i).getRefKey()))) {
						entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
						modified = true;
					}
				}
				else if (domain.get(i).getRefKey() == null) {
					entity.setReference(null);
					modified = true;
				}
				else {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefKey())));
					modified = true;
				}
				

				if (modified == true) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					synonymDAO.update(entity);
					log.info("processSynonym/changes processed: " + domain.get(i).getSynonymKey());
				}
				else {
					log.info("processSynonym/no changes processed: " + domain.get(i).getSynonymKey());
				}
			}
		}
		
		log.info("processSynonym/ran successfully");
		return;
	}
	
	@Transactional
	public void processNote(MarkerDomain domain, NoteDomain noteDomain, String mgiTypeKey, String noteTypeKey, User user) {
		// process note by calling stored procedure
		
		String noteKey;
		String note;

		if (noteDomain == null) {
			return;
		}
		
		if (noteDomain.getNoteKey() != null) {
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
		Query query = markerDAO.createNativeQuery(cmd);
		query.getResultList();
		
		return;
	}
	
	public List<MarkerEIResultDomain> eiSearch(MarkerSearchForm searchForm) {

		// list of results to be returned
		List<MarkerEIResultDomain> results = new ArrayList<MarkerEIResultDomain>();

		// parameters defined in SearchForm
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct m._marker_key, m._marker_type_key, m.symbol";
		String from = "from mrk_marker m";
		String where = "where m._organism_key = 1";
		String orderBy = "order by m._marker_type_key, m.symbol";
		String limit = "LIMIT 1000";
		Boolean from_editorNote = false;
		Boolean from_sequenceNote = false;
		Boolean from_revisionNote = false;
		Boolean from_strainNote = false;
		Boolean from_locationNote = false;
		Boolean from_accession = false;
		Boolean from_history = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification(params, "m");
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (params.containsKey("symbol")) {
			where = where + "\nand m.symbol ilike '" + params.get("symbol") + "'" ;
		}
		if (params.containsKey("name")) {
			where = where + "\nand m.name ilike '" + params.get("name") + "'" ;
		}
		if (params.containsKey("chromosome")) {
			where = where + "\nand m.chromosome = '" + params.get("chromosome") + "'" ;
		}
		if (params.containsKey("cytogeneticOffset")) {
			where = where + "\nand m.cytogeneticOffset = '" + params.get("cytogeneticOffset") + "'" ;
		}
		if (params.containsKey("cmOffset")) {
			where = where + "\nand m.cmoffset = " + params.get("cmOffset");
		}
		if (params.containsKey("markerStatusKey")) {
			where = where + "\nand m._marker_status_key = " + params.get("markerStatusKey");
		}
		if (params.containsKey("markerTypeKey")) {
			where = where + "\nand m._marker_type_key = " + params.get("markerTypeKey");
		}
		if (params.containsKey("editorNote")) {
			where = where + "\nand note1._notetype_key = 1004 and note1.note ilike '" + params.get("editorNote") + "'" ;
			from_editorNote = true;
		}
		if (params.containsKey("sequenceNote")) {
			where = where + "\nand note2._notetype_key = 1009 and note2.note ilike '" + params.get("sequenceNote") + "'" ;
			from_sequenceNote = true;
		}
		if (params.containsKey("revisionNote")) {
			where = where + "\nand note3._notetype_key = 1030 and note3.note ilike '" + params.get("revisionNote") + "'" ;
			from_revisionNote = true;
		}
		if (params.containsKey("strainNote")) {
			where = where + "\nand note4._notetype_key = 1035 and note4.note ilike '" + params.get("strainNote") + "'" ;
			from_strainNote = true;
		}
		if (params.containsKey("locationNote")) {
			where = where + "\nand note5._notetype_key = 1049 and note5.note ilike '" + params.get("locationNote") + "'" ;
			from_locationNote = true;
		}
		
		if (params.containsKey("accID")) {
			where = where + "\nand a.accID ilike '" + params.get("accID") + "'";
			from_accession = true;
		}
		
		// history
		if (params.containsKey("historySymbol")) {
			where = where + "\nand mh.history ilike '" + params.get("historySymbol") + "'";
			from_history = true;
		}
		if (params.containsKey("historyName")) {
			where = where + "\nand mh.name ilike '" + params.get("historyName") + "'";
			from_history = true;
		}
		
		if (from_accession == true) {
			// using this view to match the teleuse implementation
			from = from + ", mrk_accnoref_view a";
			where = where + "\nand m._marker_key = a._object_key" 
					+ "\nand a._mgitype_key = 2";
		}
		if (from_editorNote == true) {
			from = from + ", mgi_note_marker_view note1";
			where = where + "\nand m._marker_key = note1._object_key";
		}
		if (from_sequenceNote == true) {
			from = from + ", mgi_note_marker_view note2";
			where = where + "\nand m._marker_key = note2._object_key";
		}
		if (from_revisionNote == true) {
			from = from + ", mgi_note_marker_view note3";
			where = where + "\nand m._marker_key = note3._object_key";
		}
		if (from_strainNote == true) {
			from = from + ", mgi_note_marker_view note4";
			where = where + "\nand m._marker_key = note4._object_key";
		}
		if (from_locationNote == true) {
			from = from + ", mgi_note_marker_view note5";
			where = where + "\nand m._marker_key = note5._object_key";
		}
		if (from_history == true) {
			from = from + ", mrk_history_view mh";
			where = where + "\nand m._marker_key = mh._marker_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerEIResultDomain markerEIResultDomain = new MarkerEIResultDomain();
				markerEIResultDomain.setMarkerKey(rs.getInt("_marker_key"));
				markerEIResultDomain.setSymbol(rs.getString("symbol"));
				results.add(markerEIResultDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
	
	public MarkerEIUtilitiesDomain eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
	
		// domain object to be JSON-ed
		MarkerEIUtilitiesDomain markerEIUtilitiesDomain = new MarkerEIUtilitiesDomain();
	
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		MarkerWithdrawal markerWithdrawal = new MarkerWithdrawal();
		
		markerWithdrawal.doWithdrawal(
				(String) params.get("eventKey"),
				(String) params.get("eventReasonKey"),
				(String) params.get("oldKey"),
				(String) params.get("refKey"),
				(String) params.get("addAsSynonym"),
				(String) params.get("newName"),
				(String) params.get("newSymbol"),
				(String) params.get("newKey"));	
		
		return markerEIUtilitiesDomain;
	}
	
}
