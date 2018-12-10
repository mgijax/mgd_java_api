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
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerStatusDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerTypeDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIResultDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
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
	private OrganismDAO organismDAO;
	@Inject
	private MarkerStatusDAO markerStatusDAO;
	@Inject
	private MarkerTypeDAO markerTypeDAO;
	
	@Inject
	private NoteService noteService;
	@Inject
	private MarkerHistoryService markerHistoryService;
	@Inject
	private MGISynonymService synonymService;
	@Inject
	private MGIReferenceAssocService referenceAssocService;
	@Inject
	private AccessionService accessionService;
	
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
		results.setItem(translator.translate(entity,0));
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
		String mgiTypeName = "Marker";
		
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
		noteService.process(domain.getMarkerKey(), domain.getEditorNote(), mgiTypeKey, "1004", user);
		noteService.process(domain.getMarkerKey(), domain.getSequenceNote(), mgiTypeKey, "1009", user);
		noteService.process(domain.getMarkerKey(), domain.getRevisionNote(), mgiTypeKey, "1030", user);
		noteService.process(domain.getMarkerKey(), domain.getStrainNote(), mgiTypeKey, "1035", user);
		noteService.process(domain.getMarkerKey(), domain.getLocationNote(), mgiTypeKey, "1049", user);

		// process marker history
		markerHistoryService.process(domain.getMarkerKey(), domain.getHistory(), user);
		
		// process marker synonym
		synonymService.process(domain.getMarkerKey(), domain.getSynonyms(), mgiTypeKey, user);
		
		// process marker reference
		if (domain.getRefAssocs() != null) {
			referenceAssocService.process(domain.getMarkerKey(), domain.getRefAssocs(), mgiTypeKey, user);
		}
		
		// process marker nucleotide accession ids
		if (domain.getNucleotideAccessionIds() != null) {
			accessionService.processNucleotideAccession(domain.getMarkerKey(), domain.getNucleotideAccessionIds(), mgiTypeName, user);
		}
		
		// return entity translated to domain
		log.info("processMarker/update/returning results");
		results.setItem(translator.translate(entity, 0));
		return results;
	}

	@Transactional
	public MarkerDomain get(Integer key) {
		return translator.translate(markerDAO.get(key),0);
	}

	@Transactional
	public SearchResults<MarkerDomain> getResults(Integer key) {
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results.setItem(translator.translate(markerDAO.get(key),0));
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
	public SearchResults<MarkerDomain> delete(String key, User user) {
		// get the entity object and delete
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = markerDAO.get(Integer.valueOf(key));
		markerDAO.remove(entity);
		return results;
	}
	
	public List<MarkerEIResultDomain> eiSearch(MarkerDomain searchDomain) {

		// list of results to be returned
		List<MarkerEIResultDomain> results = new ArrayList<MarkerEIResultDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct m._marker_key, m._marker_type_key, m.symbol";
		String from = "from mrk_marker m";
		String where = "where m._organism_key = 1";
		String orderBy = "order by m._marker_type_key, m.symbol";
		String limit = "LIMIT 1000";
		String value;
		Boolean from_editorNote = false;
		Boolean from_sequenceNote = false;
		Boolean from_revisionNote = false;
		Boolean from_strainNote = false;
		Boolean from_locationNote = false;
		Boolean from_accession = false;
		Boolean from_history = false;
		Boolean from_synonym = false;
		Boolean from_reference = false;
		Boolean from_nucleotideAccession = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("m", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			where = where + "\nand m.symbol ilike '" + searchDomain.getSymbol() + "'" ;
		}
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand m.name ilike '" + searchDomain.getName() + "'" ;
		}
		if (searchDomain.getChromosome() != null && !searchDomain.getChromosome().isEmpty()) {
			where = where + "\nand m.chromosome = '" + searchDomain.getChromosome() + "'" ;
		}
		if (searchDomain.getCytogeneticOffset() != null && !searchDomain.getCytogeneticOffset().isEmpty()) {
			where = where + "\nand m.cytogeneticOffset = '" + searchDomain.getCytogeneticOffset() + "'" ;
		}
		if (searchDomain.getCmOffset() != null && !searchDomain.getCmOffset().isEmpty()) {
			where = where + "\nand m.cmoffset = " + searchDomain.getCmOffset();
		}
		if (searchDomain.getMarkerStatusKey() != null && !searchDomain.getMarkerStatusKey().isEmpty()) {
			where = where + "\nand m._marker_status_key = " + searchDomain.getMarkerStatusKey();
		}
		if (searchDomain.getMarkerTypeKey() != null && !searchDomain.getMarkerTypeKey().isEmpty()) {
			where = where + "\nand m._marker_type_key = " + searchDomain.getMarkerTypeKey();
		}
		
		// notes
		if (searchDomain.getEditorNote() != null) {
			value = searchDomain.getEditorNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note1._notetype_key = 1004 and note1.note ilike '" + value + "'" ;
			from_editorNote = true;
		}
		if (searchDomain.getSequenceNote() != null) {
			value = searchDomain.getSequenceNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note2._notetype_key = 1009 and note2.note ilike '" + value + "'" ;
			from_sequenceNote = true;
		}
		if (searchDomain.getRevisionNote() != null) {
			value = searchDomain.getRevisionNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note3._notetype_key = 1030 and note3.note ilike '" + value + "'" ;
			from_revisionNote = true;
		}
		if (searchDomain.getStrainNote() != null) {
			value = searchDomain.getStrainNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note4._notetype_key = 1035 and note4.note ilike '" + value + "'" ;
			from_strainNote = true;
		}
		if (searchDomain.getLocationNote() != null) {
			value = searchDomain.getLocationNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note5._notetype_key = 1049 and note5.note ilike '" + value + "'" ;
			from_locationNote = true;
		}
	
		// marker accession id
		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().get(0).getAccID().isEmpty()) {
			where = where + "\nand a.accID = '" + searchDomain.getMgiAccessionIds().get(0).getAccID() + "'";
			from_accession = true;
		}
		
		// history
		if (searchDomain.getHistory() != null) {
			if (searchDomain.getHistory().get(0).getMarkerHistorySymbol() != null && !searchDomain.getHistory().get(0).getMarkerHistorySymbol().isEmpty()) {
				where = where + "\nand mh.history ilike '" + searchDomain.getHistory().get(0).getMarkerHistorySymbol() + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getMarkerHistoryName() != null && !searchDomain.getHistory().get(0).getMarkerHistoryName().isEmpty()) {
				where = where + "\nand mh.name ilike '" + searchDomain.getHistory().get(0).getMarkerHistoryName() + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getEvent_date() != null && !searchDomain.getHistory().get(0).getEvent_date().isEmpty()) {
				where = where + "\nand mh.event_display = '" + searchDomain.getHistory().get(0).getEvent_date() + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getRefKey() != null && !searchDomain.getHistory().get(0).getRefKey().isEmpty()) {
				where = where + "\nand mh._Ref_key = " + searchDomain.getHistory().get(0).getRefKey();
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getShort_citation() != null && !searchDomain.getHistory().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getHistory().get(0).getShort_citation().replaceAll("'",  "''");
				where = where + "\nand mh.short_citation ilike '" + value + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getMarkerEventKey() != null && !searchDomain.getHistory().get(0).getMarkerEventKey().isEmpty()) {
				where = where + "\nand mh._Marker_Event_key = " + searchDomain.getHistory().get(0).getMarkerEventKey();
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getMarkerEventReasonKey() != null && !searchDomain.getHistory().get(0).getMarkerEventReasonKey().isEmpty()) {
				where = where + "\nand mh._Marker_EventReason_key = " + searchDomain.getHistory().get(0).getMarkerEventReasonKey();
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getModifiedBy() != null && !searchDomain.getHistory().get(0).getModifiedBy().isEmpty()) {
				where = where + "\nand mh.modifiedBy ilike '" + searchDomain.getHistory().get(0).getModifiedBy() + "'";
				from_history = true;
			}
		}

		// synonym
		if (searchDomain.getSynonyms() != null) {
			if (searchDomain.getSynonyms().get(0).getSynonym() != null && !searchDomain.getSynonyms().get(0).getSynonym().isEmpty()) {
				where = where + "\nand ms.synonym ilike '" + searchDomain.getSynonyms().get(0).getSynonym() + "'";
				from_synonym = true;
			}
			if (searchDomain.getSynonyms().get(0).getRefKey() != null && !searchDomain.getSynonyms().get(0).getRefKey().isEmpty()) {
				where = where + "\nand ms._Ref_key = " + searchDomain.getSynonyms().get(0).getRefKey();
				from_synonym = true;
			}
		}
		
		// reference
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefKey() != null && !searchDomain.getRefAssocs().get(0).getRefKey().isEmpty()) {
				where = where + "\nand mr._Ref_key = " + searchDomain.getRefAssocs().get(0).getRefKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replaceAll("'",  "''");
				where = where + "\nand mr.short_citation ilike '" + value + "'";
				from_reference = true;
			}
		}

		// nucleotideAccession
		if (searchDomain.getNucleotideAccessionIds() != null) {
			if (searchDomain.getNucleotideAccessionIds().get(0).getAccID() != null 
					&& !searchDomain.getNucleotideAccessionIds().get(0).getAccID().isEmpty()) {
				where = where + "\nand acc1.accID ilike '" +  searchDomain.getNucleotideAccessionIds().get(0).getAccID() + "'";
				from_nucleotideAccession = true;
			}
			if (searchDomain.getNucleotideAccessionIds().get(0).getReferences() != null) {
				if (searchDomain.getNucleotideAccessionIds().get(0).getReferences().get(0).getRefKey() != null 
						&& !searchDomain.getNucleotideAccessionIds().get(0).getReferences().get(0).getRefKey().isEmpty()) {
					where = where + "\nand acc1._refs_key = " + searchDomain.getNucleotideAccessionIds().get(0).getReferences().get(0).getRefKey();
					from_nucleotideAccession = true;
				}	
				if (searchDomain.getNucleotideAccessionIds().get(0).getReferences().get(0).getShort_citation() != null 
						&& !searchDomain.getNucleotideAccessionIds().get(0).getReferences().get(0).getShort_citation().isEmpty()) {
					value = searchDomain.getNucleotideAccessionIds().get(0).getReferences().get(0).getShort_citation().replaceAll("'",  "''");
					where = where + "\nand acc1.short_citation ilike '" + value + "'";
					from_nucleotideAccession = true;
				}
			}
		}
		
		// use views to match the teleuse implementation

		if (from_accession == true) {
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
			from = from + ", mgi_reference_marker_view mr";
			where = where + "\nand m._marker_key = mr._object_key";
		}
		if (from_nucleotideAccession == true) {
			from = from + ", mrk_accref1_view acc1";
			where = where + "\nand m._marker_key = acc1._object_key and acc1._logicaldb_key in (9)";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		// execute sql, returns results to MarkerEIResultDomain
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerEIResultDomain domain = new MarkerEIResultDomain();
				domain.setMarkerKey(rs.getInt("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));
				results.add(domain);
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
