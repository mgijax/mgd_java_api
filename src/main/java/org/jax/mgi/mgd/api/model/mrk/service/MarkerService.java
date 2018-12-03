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
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.NoteDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
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
	private NoteDAO noteDAO;
	@Inject
	private MarkerHistoryDAO historyDAO;
	@Inject
	private MGISynonymDAO synonymDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	//@Inject
	//private MGIReferenceAssocDAO refAssocDAO;
	
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
	
	private MarkerTranslator translator = new MarkerTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = new Marker();
		
		if (domain.getSymbol().isEmpty()) {
			entity.setSymbol(null);
		}
		else {
			entity.setSymbol(domain.getSymbol());
		}
		
		if (domain.getName().isEmpty()) {
			entity.setName(null);
		}
		else {
			entity.setName(domain.getName());
		}
		
		if (domain.getChromosome().isEmpty()) {
			entity.setChromosome(null);
		}
		else {
			entity.setChromosome(domain.getChromosome());
		}
		
		// cytoGeneticOffset always defaults to null
		// no special processing required

		// if chr = "UN", then cmOffset = -999, else cmOffset = -1
		if (domain.getChromosome().equals("UN")) {
			entity.setCmOffset(-999.0);
		}
		else {
			entity.setCmOffset(-1.0);
		}
			
		// convert String-to-Integer
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		entity.setMarkerStatus(markerStatusDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));
		entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));
		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
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
		
		// to-add/create marker synonyms, if provided
		
		// return entity translated to domain
		log.info("processMarker/create/returning results");
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
		String mgiTypeKey = "2";
		
		log.info("processMarker/update");

		//log.info("process symbol");
		if (!entity.getSymbol().equals(domain.getSymbol())) {
			log.info("process entity");
			entity.setSymbol(domain.getSymbol());
			modified = true;
		}
		
		//log.info("process name");
		if (!entity.getName().equals(domain.getName())) {
			entity.setName(domain.getName());
			modified = true;
		}
		
		//log.info("process chromosome");
		if (!entity.getChromosome().equals(domain.getChromosome())) {
			
			entity.setChromosome(domain.getChromosome());
			
			if (domain.getChromosome().equals("UN")) {
				entity.setCmOffset(-999.0);
			}
			
			modified = true;
		}
		
		//log.info("process cytogenetic offset");
		// may be null coming from entity
		if (entity.getCytogeneticOffset() == null) {
			if (domain.getCytogeneticOffset() != null) {
				entity.setCytogeneticOffset(domain.getCytogeneticOffset());
				modified = true;	
			}
		}
		// may be null coming from domain
		else if (domain.getCytogeneticOffset() == null) {
			entity.setCytogeneticOffset(null);
			modified = true;
		}
		// if not entity/null and not domain/empty, then check if equivalent
		else if (!entity.getCytogeneticOffset().equals(domain.getCytogeneticOffset())) {
			entity.setCytogeneticOffset(domain.getCytogeneticOffset());
			modified = true;
		}
	
		// cannot change the status to "withdrawn"/2
		//log.info("process marker status");
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
		
		//log.info("process marker type");
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
		NoteService.processNote(domain, domain.getEditorNote(), noteDAO, mgiTypeKey, "1004", user);
		NoteService.processNote(domain, domain.getSequenceNote(), noteDAO, mgiTypeKey, "1009", user);
		NoteService.processNote(domain, domain.getRevisionNote(), noteDAO, mgiTypeKey, "1030", user);
		NoteService.processNote(domain, domain.getStrainNote(), noteDAO, mgiTypeKey, "1035", user);
		NoteService.processNote(domain, domain.getLocationNote(), noteDAO, mgiTypeKey, "1049", user);

		// process marker history
		MarkerHistoryService.processHistory(domain.getMarkerKey(), domain.getHistory(), historyDAO, eventDAO, eventReasonDAO, referenceDAO, user);
		
		// process marker synonym
		MGISynonymService.processSynonym(domain.getMarkerKey(), domain.getSynonyms(), synonymDAO, referenceDAO, mgiTypeKey, user);
		
		// process marker reference
		//MGIReferenceAssocService.processReferenceAssoc(domain.getMarkerKey(), domain.getRefAssocs(), refAssocDAO, referenceDAO, mgiTypeKey, user);

		// return entity translated to domain
		log.info("processMarker/update/returning results");
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
		Boolean from_synonym = false;
		Boolean from_reference = false;

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
		
		// notes
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
		
		// marker accession id
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
		if (params.containsKey("historyEventDate")) {
			where = where + "\nand mh.event_display = '" + params.get("historyEventDate") + "'";
			from_history = true;
		}
		if (params.containsKey("historyRef")) {
			where = where + "\nand mh._Ref_key = " + params.get("historyRef");
			from_history = true;
		}
		if (params.containsKey("historyShortCitation")) {
			where = where + "\nand mh.short_citation ilike '" + params.get("historyShortCitation") + "'";
			from_history = true;
		}
		if (params.containsKey("historyEvent")) {
			where = where + "\nand mh.event ilike '" + params.get("historyEvent") + "'";
			from_history = true;
		}
		if (params.containsKey("historyEventReason")) {
			where = where + "\nand mh.eventReason ilike '" + params.get("historyEventReason") + "'";
			from_history = true;
		}
		if (params.containsKey("historyModifiedBy")) {
			where = where + "\nand mh.modifiedBy ilike '" + params.get("historyModifiedBy") + "'";
			from_history = true;
		}

		// synonym
		if (params.containsKey("synonymName")) {
			where = where + "\nand ms.synonym ilike '" + params.get("synonymName") + "'";
			from_synonym = true;
		}
		if (params.containsKey("synonymRefKey")) {
			where = where + "\nand ms._Ref_key = " + params.get("synonymRefKey");
			from_synonym = true;
		}
		
		// reference
		if (params.containsKey("refAssocRefKey")) {
			where = where + "\nand mr._Ref_key = " + params.get("refAssocRefKey");
			from_reference = true;
		}
		if (params.containsKey("refAssocShortCitation")) {
			where = where + "\nand mr.short_citation ilike '" + params.get("refAssocShortCitation") + "'";
			from_reference = true;
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
		if (from_synonym == true) {
			from = from + ", mgi_synonym_musmarker_view ms";
			where = where + "\nand m._marker_key = ms._object_key";
		}
		if (from_reference == true) {
			from = from + ", mrk_reference_marker_view mr";
			where = where + "\nand m._marker_key = mr._object_key";
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
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
