package org.jax.mgi.mgd.api.model.mrk.service;

import java.io.IOException;
import java.sql.ResultSet;
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
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
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
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistoryKey;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTranslator;
import org.jax.mgi.mgd.api.util.Constants;
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
		String cmd = "select count(*) from MRK_insertHistory ("
				+ user.get_user_key().intValue()
				+ "," + entity.get_marker_key()
				+ "," + entity.get_marker_key()
				+ ",22864,1,-1"
				+ ",'" + entity.getName() + "'"
				+ ")";

		log.info("cmd: " + cmd);
		Query query = markerDAO.createNativeQuery(cmd);
		query.getResultList();
		
		//MarkerHistory historyEntity = new MarkerHistory();
		
		// set primary key = compound/embedded id (new marker key, sequenceNum = 1)
		// default marker event = 1 (assigned)
		// default marker event reason = -1 (Not Specified)
		// default reference = 22864 (J:23000)
		// default history symbol key = new marker key (same as primary)
		// default history name = marker name
		
		//MarkerHistoryKey markerHistoryKey = new MarkerHistoryKey();
		//markerHistoryKey.set_marker_key(entity.get_marker_key());
		//markerHistoryKey.setSequenceNum(1);
		//historyEntity.setKey(markerHistoryKey);
		// rest of marker attributes
		//historyEntity.setMarkerEvent(eventDAO.get(Integer.valueOf(1)));
		//historyEntity.setMarkerEventReason(eventReasonDAO.get(Integer.valueOf(-1)));
		//historyEntity.setMarkerHistory(entity);
		//historyEntity.setReference(referenceDAO.get(Integer.valueOf(22864)));
		//historyEntity.setName(entity.getName());
		//historyEntity.setEvent_date(new Date());
		//historyEntity.setCreatedBy(user);
		//historyEntity.setCreation_date(new Date());
		//historyEntity.setModifiedBy(user);
		//historyEntity.setModification_date(new Date());
		//historyDAO.persist(historyEntity);
		// end marker history 
		
		// create marker synonyms, if provided
		
		// return entity translated to domain
		results.setItem(translator.translate(entity));
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerDomain> update(MarkerDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// fields that are not be updated by UI are commented out below
		// creation user/date are only set in "create"

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = markerDAO.get(domain.getMarkerKey());
		
		// set entity fields
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		entity.setChromosome(domain.getChromosome());
		entity.setCytogeneticOffset(domain.getCytogeneticOffset());
		// end set entity fields
		
		// business rules
		
		// cannot change the status to "withdrawn"/2
		if (entity.getMarkerStatus().getStatus().equals(domain.getMarkerStatus()) == false) {
			if (domain.getMarkerStatusKey().equals("2")) {
				results.setError("Failed : Marker Status error",  "Cannot change Marker Status to 'withdrawn'", Constants.HTTP_SERVER_ERROR);
				return results;
			}
		}
		
		// for cmOffset
		if (domain.getChromosome().equals("UN")) {
			entity.setCmOffset(-999.0);
		}
		
		// end business logic
		
		// convert String-to-Integer
		//entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		entity.setMarkerStatus(markerStatusDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));
		entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));
		// end String-to-Integer conversion
		
		// add creation/modification 
		//entity.setCreatedBy(user);
		//entity.setCreation_date(new Date());
		entity.setModification_date(new Date());
		entity.setModifiedBy(user);
		// end creation/modification
		
		// execute update/send to database
		markerDAO.update(entity);
		
		// process all marker notes
		processNote(domain, domain.getEditorNote(), "2", "1004", user);
		processNote(domain, domain.getSequenceNote(), "2", "1009", user);
		processNote(domain, domain.getRevisionNote(), "2", "1030", user);
		processNote(domain, domain.getStrainNote(), "2", "1035", user);
		processNote(domain, domain.getLocationNote(), "2", "1049", user);

		// add markerHistoryDAO
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
	public void processHistory(MarkerDomain domain, User user) {
		// create marker history assignment
		// create 1 marker history row to track the initial marker assignment
		//String cmd = "select count(*) from MGI_insertHistory ("
		//		+ user.get_user_key().intValue()
		//		+ "," + entity.get_marker_key()
		//		+ "," + domain.getMarkerKey()
		//		+ ",22864,1,-1"
		//		+ "," + entity.getName()
		//		+ ",now()"
		//		+ "," + user.get_user_key().intValue()
		//		+ "," + user.get_user_key().intValue()
		//		+ ",now(),now()"
		//		+ ")";

		//log.info("cmd: " + cmd);
		//Query query = markerDAO.createNativeQuery(cmd);
		//query.getResultList();
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
	
		if (noteDomain.getNoteChunk() != null || noteDomain.getNoteChunk() != "") {
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
		Boolean from_user1 = false;
		Boolean from_user2 = false;
		Boolean from_accession = false;

		// if parameter exists, then add to where-clause
		
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
		//if (params.containsKey("creation_date")) {
		//	if (params.get("creation_date").toString().startsWith("<=") == true) {
		//		where = where + "\nand m.creation_date <= '" 
		//				+ params.get("creation_date").toString().replace("<=","") + "'";
		//	}
		//	else if (params.get("creation_date").toString().startsWith("<") == true) {
		//		where = where + "\nand m.creation_date < '" 
		//				+ params.get("creation_date").toString().replace("<",  "") + "'";
		//	}
		//	else if (params.get("creation_date").toString().startsWith(">=") == true) {
		//		where = where + "\nand m.creation_date >= '" 
		//				+ params.get("creation_date").toString().replace(">=","") + "'";
		//	}
		//	else if (params.get("creation_date").toString().startsWith(">") == true) {
		//		where = where + "\nand m.creation_date > '" 
		//				+ params.get("creation_date").toString().replace(">",  "") + "'";
		//	}
		//	else {
		//		where = where + "\nand (m.creation_date between '" 
		//				+ params.get("creation_date") 
		//				+ "' and ('" + params.get("creation_date")
		//				+ "'::date + '1 day'::interval))";
		//	}
		//}
		if (params.containsKey("createdBy")) {
			where = where + "\nand u1.login ilike '" + params.get("createdBy") + "'";
			from_user1 = true;
		}
		if (params.containsKey("modifiedBy")) {
			where = where + "\nand u2.login ilike '" + params.get("modifiedBy") + "'";
			from_user2 = true;
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
		
		// if parameter was added to where-clause, then add table (only once) to from-clause
		// tables and views are allowed
		// this could also be done as a series of "exists" clauses
		
		if (from_user1 == true) {
			from = from + ", mgi_user u1";
			where = where + "\nand m._createdBy_key = u1._user_key";
		}
		if (from_user2 == true) {
			from = from + ", mgi_user u2";
			where = where + "\nand m._modifiedBy_key = u2._user_key";
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
