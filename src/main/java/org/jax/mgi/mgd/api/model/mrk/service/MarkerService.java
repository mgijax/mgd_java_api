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
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerOfficialChromDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.RunCommand;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerService extends BaseService<MarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());

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
	@Inject
	private AnnotationService annotationService;

	
	private MarkerTranslator translator = new MarkerTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = new Marker();
		
		// assumes that required fields exist
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		entity.setChromosome(domain.getChromosome());
		
		// cytoGeneticOffset always defaults to null
		// no special processing required

		// if chr = "UN", then cmOffset = -999, else cmOffset = -1
		if (domain.getChromosome().equals("UN")) {
			entity.setCmOffset(-999.0);
		}
		else {
			entity.setCmOffset(-1.0);
		}
			
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		
		// marker status cannot be "withdrawn"
		if (domain.getMarkerStatusKey().equals("2")) {
			results.setError("Failed : Marker Status error",  "Cannot use 'withdrawn'", Constants.HTTP_SERVER_ERROR);
			return results;
		}
		else {
			entity.setMarkerStatus(markerStatusDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));
		}
		
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
		if (domain.getHistory().get(0).getRefsKey() != null && domain.getHistory().get(0).getRefsKey().isEmpty()) {
			refKey = "22864";
		}
		else {
			refKey = domain.getHistory().get(0).getRefsKey().toString();
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
		if (!String.valueOf(entity.getMarkerStatus().get_marker_status_key()).equals(domain.getMarkerStatusKey())) {
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
		if (!String.valueOf(entity.getMarkerType().get_marker_type_key()).equals(domain.getMarkerTypeKey())) {
			entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));
			modified = true;
		}
		
		// process all notes
		if (noteService.process(domain.getMarkerKey(), domain.getEditorNote(), mgiTypeKey, "1004", user)) {
			modified = true;
		}
		if (noteService.process(domain.getMarkerKey(), domain.getSequenceNote(), mgiTypeKey, "1009", user)) {
			modified = true;	
		}
		if (noteService.process(domain.getMarkerKey(), domain.getRevisionNote(), mgiTypeKey, "1030", user)) {
			modified = true;	
		}
		if (noteService.process(domain.getMarkerKey(), domain.getStrainNote(), mgiTypeKey, "1035", user)) {
			modified = true;
		}
		if (noteService.process(domain.getMarkerKey(), domain.getLocationNote(), mgiTypeKey, "1049", user)) {
			modified = true;
		}

		// process marker history
		if (markerHistoryService.process(domain.getMarkerKey(), domain.getHistory(), user)) {
			modified = true;
		}
		
		// process marker synonym
		if (synonymService.process(domain.getMarkerKey(), domain.getSynonyms(), mgiTypeKey, user)) {
			modified = true;
		}
		
		// process marker reference
		if (domain.getRefAssocs() != null && !domain.getRefAssocs().isEmpty()) {
			if (referenceAssocService.process(domain.getMarkerKey(), domain.getRefAssocs(), mgiTypeKey, user)) {
				modified = true;
			}
		}
		
		// process marker nucleotide accession ids
		if (domain.getEditAccessionIds() != null && !domain.getEditAccessionIds().isEmpty()) {
			if (accessionService.process(domain.getMarkerKey(), domain.getEditAccessionIds(), mgiTypeName, user)) {
				modified = true;
			}
		}

		// process feature types
		// use qualifier 'Generic Annotation Qualifier', value = null
		if (domain.getFeatureTypes() != null && !domain.getFeatureTypes().isEmpty()) {
			if (annotationService.processMarkerFeatureType(domain.getMarkerKey(), 
					domain.getFeatureTypes(), 
					domain.getFeatureTypes().get(0).getAnnotTypeKey(),
					Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user) == true) {
				modified = true;			
			}
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
				
		// return entity translated to domain
		log.info("processMarker/update/returning results");
		results.setItem(translator.translate(entity, 0));
		log.info("processMarker/update/returned results succsssful");
		return results;
	}

	@Transactional
	public MarkerDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MarkerDomain domain = new MarkerDomain();
		if (markerDAO.get(key) != null) {
			domain = translator.translate(markerDAO.get(key),1);
		}
		return domain;
	}

	@Transactional
	public SearchResults<MarkerDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results.setItem(translator.translate(markerDAO.get(key),0));
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = markerDAO.get(key);
		results.setItem(translator.translate(markerDAO.get(key),0));
		markerDAO.remove(entity);
		return results;
	}

	@Transactional	
	public List<SlimMarkerDomain> eiSearch(MarkerDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

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
		Boolean from_editAccession = false;
		Boolean from_noneditAccession = false;
		Boolean from_featureTypes = false;		
		Boolean from_tss1 = false;
		Boolean from_alias = false;

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
			where = where + "\nand a.accID ilike '" + searchDomain.getMgiAccessionIds().get(0).getAccID() + "'";
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
			if (searchDomain.getHistory().get(0).getRefsKey() != null && !searchDomain.getHistory().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand mh._Ref_key = " + searchDomain.getHistory().get(0).getRefsKey();
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getJnumid() != null && !searchDomain.getHistory().get(0).getJnumid().isEmpty()) {
				where = where + "\nand mh.jnumid = '" + searchDomain.getHistory().get(0).getJnumid() + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getShort_citation() != null && !searchDomain.getHistory().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getHistory().get(0).getShort_citation().replaceAll("'",  "''");
				where = where + "\nand mh.short_citation ilike '" + value + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getMarkerEvent() != null && !searchDomain.getHistory().get(0).getMarkerEvent().isEmpty()) {
				where = where + "\nand mh.event = '" + searchDomain.getHistory().get(0).getMarkerEvent() + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getMarkerEventReason() != null && !searchDomain.getHistory().get(0).getMarkerEventReason().isEmpty()) {
				where = where + "\nand mh.eventreason = '" + searchDomain.getHistory().get(0).getMarkerEventReason() + "'";
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
		// synonymTypeKey, synonym, j:, modified by, modification date
		if (searchDomain.getSynonyms() != null) {
			if (searchDomain.getSynonyms().get(0).getSynonymTypeKey() != null && !searchDomain.getSynonyms().get(0).getSynonymTypeKey().isEmpty()) {
				where = where + "\nand ms._synonymtype_key = " + searchDomain.getSynonyms().get(0).getSynonymTypeKey();
				from_synonym = true;
			}
			if (searchDomain.getSynonyms().get(0).getSynonym() != null && !searchDomain.getSynonyms().get(0).getSynonym().isEmpty()) {
				where = where + "\nand ms.synonym ilike '" + searchDomain.getSynonyms().get(0).getSynonym() + "'";
				from_synonym = true;
			}
			if (searchDomain.getSynonyms().get(0).getJnumid() != null && !searchDomain.getSynonyms().get(0).getJnumid().isEmpty()) {
				where = where + "\nand ms.jnumid ilike '" + searchDomain.getSynonyms().get(0).getJnumid() + "'";
				from_synonym = true;
			}
			String synModifiedBy[] = 
					DateSQLQuery.queryByCreationModification("ms", 
							searchDomain.getSynonyms().get(0).getCreatedBy(), 
							searchDomain.getSynonyms().get(0).getModifiedBy(), 
							searchDomain.getSynonyms().get(0).getCreation_date(), 
							searchDomain.getSynonyms().get(0).getModification_date());
			if (synModifiedBy.length > 0) {
				if (!synModifiedBy[0].isEmpty() || !synModifiedBy[1].isEmpty()) {
					from = from + synModifiedBy[0];
					where = where + synModifiedBy[1];
					from_synonym = true;
				}
			}			
		}
		
		// reference
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand mr._Ref_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replaceAll("'",  "''");
				where = where + "\nand mr.short_citation ilike '" + value + "'";
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getJnumid() != null && !searchDomain.getRefAssocs().get(0).getJnumid().isEmpty()) {
				where = where + "\nand mr.jnumid ilike '" + searchDomain.getRefAssocs().get(0).getJnumid() + "'";
				from_reference = true;
			}		
			String refModifiedBy[] = 
					DateSQLQuery.queryByCreationModification("mr", 
							searchDomain.getRefAssocs().get(0).getCreatedBy(), 
							searchDomain.getRefAssocs().get(0).getModifiedBy(), 
							searchDomain.getRefAssocs().get(0).getCreation_date(), 
							searchDomain.getRefAssocs().get(0).getModification_date());
			if (refModifiedBy.length > 0) {
				if (!refModifiedBy[0].isEmpty() || !refModifiedBy[1].isEmpty()) {
					from = from + refModifiedBy[0];
					where = where + refModifiedBy[1];
					from_reference = true;
				}
			}
			if (searchDomain.getRefAssocs().get(0).getRefAssocTypeKey() != null && !searchDomain.getRefAssocs().get(0).getRefAssocTypeKey().isEmpty()) {
				where = where + "\nand mr._refassoctype_key = " + searchDomain.getRefAssocs().get(0).getRefAssocTypeKey();
				from_reference = true;
			}			
		}

		// editable accession ids
		if (searchDomain.getEditAccessionIds() != null) {
			if (searchDomain.getEditAccessionIds().get(0).getAccID() != null 
					&& !searchDomain.getEditAccessionIds().get(0).getAccID().isEmpty()) {
				where = where + "\nand acc1.accID ilike '" +  searchDomain.getEditAccessionIds().get(0).getAccID() + "'";
				from_editAccession = true;
			}
			if (searchDomain.getEditAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getEditAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
				where = where + "\nand acc1._logicaldb_key = " + searchDomain.getEditAccessionIds().get(0).getLogicaldbKey();
				from_editAccession = true;
			}
			if (searchDomain.getEditAccessionIds().get(0).getReferences() != null) {
				if (searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getRefsKey() != null 
						&& !searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getRefsKey().isEmpty()) {
					where = where + "\nand acc1._refs_key = " + searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getRefsKey();
					from_editAccession = true;
				}	
				if (searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getShort_citation() != null 
						&& !searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getShort_citation().isEmpty()) {
					value = searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getShort_citation().replaceAll("'",  "''");
					where = where + "\nand acc1.short_citation ilike '" + value + "'";
					from_editAccession = true;
				}
				if (searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getJnumid() != null && !searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getJnumid().isEmpty()) {
					where = where + "\nand acc1.jnumid ilike '" + searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getJnumid() + "'";
					from_editAccession = true;
				}					
			}
			String editAccModifiedBy[] = 
					DateSQLQuery.queryByCreationModification("acc1", 
							searchDomain.getEditAccessionIds().get(0).getCreatedBy(), 
							searchDomain.getEditAccessionIds().get(0).getModifiedBy(), 
							searchDomain.getEditAccessionIds().get(0).getCreation_date(), 
							searchDomain.getEditAccessionIds().get(0).getModification_date());
			if (editAccModifiedBy.length > 0) {
				if (!editAccModifiedBy[0].isEmpty() || !editAccModifiedBy[1].isEmpty()) {
					from = from + editAccModifiedBy[0];
					where = where + editAccModifiedBy[1];
					from_editAccession = true;
				}
			}			
		}
		
		// non-editable accession ids
		if (searchDomain.getNonEditAccessionIds() != null) {
			if (searchDomain.getNonEditAccessionIds().get(0).getAccID() != null 
					&& !searchDomain.getNonEditAccessionIds().get(0).getAccID().isEmpty()) {
				where = where + "\nand acc2.accID ilike '" +  searchDomain.getNonEditAccessionIds().get(0).getAccID() + "'";
				from_noneditAccession = true;
			}
			if (searchDomain.getNonEditAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getNonEditAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
				where = where + "\nand acc2._logicaldb_key = " + searchDomain.getNonEditAccessionIds().get(0).getLogicaldbKey();
				from_noneditAccession = true;
			}
			if (searchDomain.getNonEditAccessionIds().get(0).getReferences() != null) {
				if (searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getRefsKey() != null 
						&& !searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getRefsKey().isEmpty()) {
					where = where + "\nand acc2._refs_key = " + searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getRefsKey();
					from_noneditAccession = true;
				}	
				if (searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getShort_citation() != null 
						&& !searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getShort_citation().isEmpty()) {
					value = searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getShort_citation().replaceAll("'",  "''");
					where = where + "\nand acc2.short_citation ilike '" + value + "'";
					from_noneditAccession = true;
				}
				if (searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getJnumid() != null && !searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getJnumid().isEmpty()) {
					where = where + "\nand acc2.jnumid ilike '" + searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getJnumid() + "'";
					from_noneditAccession = true;
				}					
			}
			String noneditAccModifiedBy[] = 
					DateSQLQuery.queryByCreationModification("acc2", 
							searchDomain.getNonEditAccessionIds().get(0).getCreatedBy(), 
							searchDomain.getNonEditAccessionIds().get(0).getModifiedBy(), 
							searchDomain.getNonEditAccessionIds().get(0).getCreation_date(), 
							searchDomain.getNonEditAccessionIds().get(0).getModification_date());
			if (noneditAccModifiedBy.length > 0) {
				if (!noneditAccModifiedBy[0].isEmpty() || !noneditAccModifiedBy[1].isEmpty()) {
					from = from + noneditAccModifiedBy[0];
					where = where + noneditAccModifiedBy[1];
					from_noneditAccession = true;
				}
			}			
		}
				
		// by _term_key only
		if (searchDomain.getFeatureTypes() != null) {
			where = where + "\nand v._term_key = " + searchDomain.getFeatureTypes().get(0).getTermKey();
			from_featureTypes = true;
		}
		
		if (searchDomain.getTssToGene() != null) {
			where = where + "\nand (tss1.marker1 ilike '" + searchDomain.getTssToGene().get(0).getSymbol() + "'" +
						"or tss1.marker2 ilike '" + searchDomain.getTssToGene().get(0).getSymbol() + "')";
			from_tss1 = true;
		}

		if (searchDomain.getAliases() != null) {
			where = where + "\nand alias.symbol ilike '" + searchDomain.getAliases().get(0).getSymbol() + "'";
			from_alias = true;
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
		if (from_editAccession == true) {
			from = from + ", mrk_accref1_view acc1";
			where = where + "\nand m._marker_key = acc1._object_key";
		}
		if (from_noneditAccession == true) {
			from = from + ", mrk_accref2_view acc2";
			where = where + "\nand m._marker_key = acc2._object_key";
		}		
		if (from_featureTypes == true) {
			from = from + ", voc_annot v";
			where = where + "\nand m._marker_key = v._object_key" 
					+ "\nand v._annottype_key = 1011";
		}
		if (from_tss1 == true) {
			from = from + ", mgi_relationship_markertss_view tss1";
			where = where + "\nand (m._marker_key = tss1._object_key_1 or m._marker_key = tss1._object_key_2)";
		}
		if (from_alias == true) {
			from = from + ", mrk_alias_View alias";
			where = where + "\nand m._marker_key = alias._alias_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimMarkerDomain domain = new SlimMarkerDomain();
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));
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
	public List<SlimMarkerDomain> getAlias(Integer key) {
		// use SlimMarkerDomain to return list of marker/alias associations
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		String cmd = "\nselect * from mrk_alias_view"
				+ "\nwhere _alias_key = " + key
				+ "\norder by symbol";	
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {				
				SlimMarkerDomain domain = new SlimMarkerDomain();				
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));				
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
	public List<SlimMarkerDomain> validate(String value, Boolean allowWithdrawn, Boolean allowReserved) {
		// use SlimMarkerDomain to return list of validated marker
		// one value is expected
		// organism = 1 (mouse) expected
		// returns empty list if value contains "%"
		// returns empty list if value does not exist

		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();
		
		if (value.contains("%")) {
			return results;
		}

		String cmd = "\nselect _marker_key, symbol"
				+ "\nfrom mrk_marker"
				+ "\nwhere _organism_key = 1"
				+ "\nand lower(symbol) = '" + value.toLowerCase() + "'";
		
		if (allowWithdrawn == false) {
			cmd = cmd + "\nand _marker_status_key not in (2)";
		}

		if (allowReserved == false) {
			cmd = cmd + "\nand _marker_status_key not in (3)";
		}
				
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimMarkerDomain domain = new SlimMarkerDomain();						
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));			
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
	public SearchResults<SlimMarkerOfficialChromDomain> validateOfficialChrom(SlimMarkerOfficialChromDomain searchDomain) {
		// use SlimMarkerOfficialChromDomain to return list of validated markers
		// marker1 = old/existing marker
		// marker2 = new marker; the one that really needs validation
		// organism = 1 (mouse) expected
		// marker status = official
		// chromosomes of marker1 must match chromosome of marker2
		// returns empty list of values if validation fails

		SearchResults<SlimMarkerOfficialChromDomain> results = new SearchResults<SlimMarkerOfficialChromDomain>();
		List<SlimMarkerOfficialChromDomain> listOfResults = new ArrayList<SlimMarkerOfficialChromDomain>();
	
		String cmd = "\nselect m1._marker_key as markerKey1, m1.symbol as symbol1, m1.chromosome as chromosome1"
				+ ",m2._marker_key as markerKey2, m2.symbol as symbol2, m2.chromosome as chromosome2"
				+ ", a.accID"
				+ "\nfrom mrk_marker m1, mrk_marker m2, acc_accession a"
				+ "\nwhere m1._organism_key = 1"
				+ "\nand m1._marker_status_key = 1"
				+ "\nand m1._marker_key = " + searchDomain.getMarkerKey1()
				+ "\nand m2._organism_key = 1"
				+ "\nand m2._marker_status_key = 1"		
				+ "\nand m1.chromosome = '" + searchDomain.getChromosome1() + "'"
				+ "\nand m1.chromosome = m2.chromosome" 
				+ "\nand m2._marker_key = a._object_key"
				+ "\nand a._mgitype_key = 2"
				+ "\nand a._logicaldb_key = 1"
				+ "\nand a.preferred = 1";
				
		if (searchDomain.getSymbol2() != null && !searchDomain.getSymbol2().isEmpty()) {
			cmd = cmd + "\nand lower(m2.symbol) = '" + searchDomain.getSymbol2().toLowerCase() + "'";
		}
		if (searchDomain.getMgiAccId2() != null && !searchDomain.getMgiAccId2().isEmpty()) {
			cmd = cmd + "\nand a.accID = '" + searchDomain.getMgiAccId2() + "'";
		}
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimMarkerOfficialChromDomain domain = new SlimMarkerOfficialChromDomain();						
				domain.setMarkerKey1(rs.getString("markerKey1"));
				domain.setSymbol1(rs.getString("symbol1"));
				domain.setChromosome1(rs.getString("chromosome1"));	
				domain.setMarkerKey2(rs.getString("markerKey2"));
				domain.setSymbol2(rs.getString("symbol2"));
				domain.setChromosome2(rs.getString("chromosome2"));		
				domain.setMgiAccId2(rs.getString("accID"));
				listOfResults.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		results.setItems(listOfResults);
		
		if (results.items.isEmpty()) {
			results.setError(Constants.LOG_MGI_API, "Check New Marker/must be Official/Chromosomes must match", Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
	}	
	
	@Transactional	
	public SearchResults<SlimMarkerFeatureTypeDomain> validateFeatureTypes(SlimMarkerFeatureTypeDomain searchDomain) {
		// use SlimMarkerFeatureTypeDomain to return list of validated markers
		// check that markerTypeKey and featureType.term are valid
		// returns empty list of values if validation fails
		
		SearchResults<SlimMarkerFeatureTypeDomain> results = new SearchResults<SlimMarkerFeatureTypeDomain>();
		List<SlimTermDomain> terms = new ArrayList<SlimTermDomain>();
		String markerTypeKey = searchDomain.getMarkerTypeKey();
		Boolean validation = true;
		
		// if a new Feature Type is added for Gene, then nothing needs to be added
		// 1:Gene : nothing special needs to be done
		// verification will fail if any of the following is true...

		// if a new Feature Type is added for non-Gene marker type,
		// then add Feature TYpe term to specific marker type below
		
		// using any of these feature types is invalid
		// 2:DNA Segment—no feature type
		// 6:QTL—no feature type
		// 8:BAC/YAC end—no feature type
		// 10:Complex/Cluster/Region—no feature type
		// 12:Transgene—no feature type
		
		if (markerTypeKey.equals("2")
				|| markerTypeKey.equals("6")
				|| markerTypeKey.equals("8")
				|| markerTypeKey.equals("10")
				|| markerTypeKey.equals("12")) {
			validation = false;
		}
		else {

			//
			// for each term in SlimMarkerFeatureTypeDomain.featureTypes()
			// check if Feature Type is OK for SlimMarkerFeatureTypeDomain.markerTypeKey
			//       
			terms = searchDomain.getFeatureTypes();
			for (int i = 0; i < terms.size(); i++) {
			
				String term = terms.get(i).getTerm();
								
				// 3:Cytogenetic Marker
				if (!markerTypeKey.equals("3") &&
						(term.equals("chromosomal deletion")
						|| term.equals("chromosomal duplication")
						|| term.equals("chromosomal inversion")
						|| term.equals("chromosomal translocation")
						|| term.equals("chromosomal transposition")
						|| term.equals("insertion")
						|| term.equals("reciprocal chromosomal translocation")
						|| term.equals("Robertsonian fusion")
						|| term.equals("unclassified cytogenetic marker"))) {
					validation = false;
				}
				// 7:Pseudogene
				else if (!markerTypeKey.equals("7") &&
						(term.equals("polymorphic pseudogene")
						|| term.equals("pseudogene")
						|| term.equals("pseudogenic gene segment")
						|| term.equals("pseudogenic region"))) {
					validation = false;
				}
				// 9:Other Genome Feature
				else if (!markerTypeKey.equals("9") &&
						(term.equals("polymorphic pseudogene")
							|| term.equals("endogenous retroviral region")
							|| term.equals("minisatellite")
							|| term.equals("promoter")
							|| term.equals("retrotransposon")
							|| term.equals("telomere")
							|| term.equals("TSS region")							
							|| term.equals("unclassified other genome feature"))) {
					validation = false;
				}
			}
		}
		
		// the items can be left empty
		//results.setItem(searchDomain);
		
		if (validation == false) {
			results.setError(Constants.LOG_MGI_API, "Invalid Marker Type/Feature Type combination", Constants.HTTP_SERVER_ERROR);
		}
		
		return results;
	}	
		
	@Transactional		
	public SearchResults<SlimMarkerDomain> eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
	
//		required for all cases/set by user
//		eventKey : MR_Event list
//		eventReasonKey : MRK_EventReason list
//		refKey  :  reference
//
//		merge, allele-of
//		addAsSynonym :  1 or 0
//		oldKey : existing marker key 
//		newKey : marker key of the marker being merged into
//		newSymbol : null 
//		newName : null 
//
//		rename
//		addAsSynonym :  1 or 0
//		oldKey : existing marker key 
//		newKey : null
//		newSymbol : marker symbol 
//		newName : marker name 
//
//		delete:
//		addAsSynonym : 0
//		oldKey : existing marker key 
//		newKey : null
//		newSymbol : null 
//		newName : null 
//
		
		// these swarm variables are in 'app.properties'
    	String eiUtilitiesScript = System.getProperty("swarm.ds.eiUtilities");
    	String server = System.getProperty("swarm.ds.dbserver");
        String db = System.getProperty("swarm.ds.dbname");
        String user = System.getProperty("swarm.ds.username");
        String pwd = System.getProperty("swarm.ds.dbpasswordfile");
        
        // input:  searchForm parameters
		Map<String, Object> params = searchForm.getSearchFields();

        // output: results
		SearchResults<SlimMarkerDomain> results = new SearchResults<SlimMarkerDomain>();
		List<SlimMarkerDomain> listOfResults = new ArrayList<SlimMarkerDomain>();

		String runCmd = eiUtilitiesScript;
        runCmd = runCmd + " -S" + server;
        runCmd = runCmd + " -D" + db;
        runCmd = runCmd + " -U" + user;
        runCmd = runCmd + " -P" + pwd;
        runCmd = runCmd + " --eventKey=" + (String) params.get("eventKey");
        runCmd = runCmd + " --eventReasonKey=" + (String) params.get("eventReasonKey");
        runCmd = runCmd + " --oldKey=" + (String) params.get("oldKey");
        runCmd = runCmd + " --refKey=" + (String) params.get("refKey");
        runCmd = runCmd + " --addAsSynonym=" + (String) params.get("addAsSynonym");

		// rename/delete is "oldKey"
		String key = (String) params.get("oldKey");

		// mrk_event = rename
		if (params.get("eventKey").equals("2")) {
			runCmd = runCmd + " --newName='" + (String) params.get("newName") + "'";
			runCmd = runCmd + " --newSymbols='" + (String) params.get("newSymbol") + "'";
		}
		
		// mrk_event = merge
		if (params.get("eventKey").equals("3") || params.get("eventKey").equals("4")) {
			runCmd = runCmd + " --newKey=" + (String) params.get("newKey");
			key = (String) params.get("newKey");
		}
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);			 			
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);	
			results.setError(Constants.LOG_FAIL_EIUTILITIES, runner.getStdErr(), Constants.HTTP_SERVER_ERROR);
		}			

		// regardless of exit code from RunCommand
		// select set of markers where current marker = marker being processed
		String queryCmd = "\nselect _marker_key, symbol from mrk_current_view"
				+ "\nwhere _current_key = " + key
				+ "\norder by symbol";	
		log.info(queryCmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(queryCmd);
			while (rs.next()) {				
				SlimMarkerDomain domain = new SlimMarkerDomain();				
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));				
				listOfResults.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// return list of results returned from query
		// note that any run command errors have already been attached
		results.setItems(listOfResults);
		return results;
	}
	
}
