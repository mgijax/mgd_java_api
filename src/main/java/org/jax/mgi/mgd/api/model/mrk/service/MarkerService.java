package org.jax.mgi.mgd.api.model.mrk.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.jax.mgi.mgd.api.model.mrk.domain.SummaryMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTranslator;
import org.jax.mgi.mgd.api.model.mrk.translator.SlimMarkerTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.RunCommand;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

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
	private SlimMarkerTranslator slimtranslator = new SlimMarkerTranslator();

	String mgiTypeKey = "2";
	String mgiTypeName = "Marker";
	
	@Transactional
	public SearchResults<MarkerDomain> create(MarkerDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = new Marker();
		
		// default marker type = 1/gene
		if (domain.getMarkerTypeKey() == null || domain.getMarkerTypeKey().isEmpty()) {
			domain.setMarkerTypeKey("1");
		}
		entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));			

		// default marker status = 1/official
		if (domain.getMarkerStatusKey() == null || domain.getMarkerStatusKey().isEmpty()) {
			domain.setMarkerStatusKey("1");
		}
		
		// marker status cannot be "withdrawn"		
		if (domain.getMarkerStatusKey().equals("2")) {
			results.setError("Failed : Marker Status error",  "Cannot use 'withdrawn'", Constants.HTTP_SERVER_ERROR);
			return results;
		}
		else {
			entity.setMarkerStatus(markerStatusDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));
		}
		
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		entity.setChromosome(domain.getChromosome().toUpperCase());		
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));

		// default cmOffset
		if (domain.getChromosome().equals("UN")) {
			entity.setCmOffset(-999.0);
		}
		else {
			entity.setCmOffset(-1.0);
		}
	
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		markerDAO.persist(entity);

		// process marker accession ids that can be edited
		if (domain.getEditAccessionIds() != null && !domain.getEditAccessionIds().isEmpty()) {
			accessionService.process(String.valueOf(entity.get_marker_key()), domain.getEditAccessionIds(), mgiTypeName, user);
		}
		
		// mouse only stuff		
		if (domain.getOrganismKey().equals("1")) {		

			// process feature types
			// use qualifier 'Generic Annotation Qualifier', value = null
			if (domain.getFeatureTypes() != null && !domain.getFeatureTypes().isEmpty()) {
				if (domain.getFeatureTypes().get(0).getTermKey() != null && !domain.getFeatureTypes().get(0).getTermKey().isEmpty()) {
					annotationService.processMarkerFeatureType(String.valueOf(entity.get_marker_key()), 
						domain.getFeatureTypes(), 
						domain.getFeatureTypes().get(0).getAnnotTypeKey(),
						Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user);
				}
			}
			
			// process marker synonym
			if (domain.getSynonyms() != null) {
				synonymService.process(String.valueOf(entity.get_marker_key()), domain.getSynonyms(), mgiTypeKey, user);
			}
			
			// create marker history assignment
			// create 1 marker history row to track the initial marker assignment		
			// event = assigned (106563604)
			// event reason = Not Specified (106563610)
			// default reference is J:23000 (22864); sent by UI
			
			String cmd = "select count(*) from MRK_insertHistory ("
					+ user.get_user_key().intValue()
					+ "," + entity.get_marker_key()
					+ "," + entity.get_marker_key()
					+ "," + domain.getHistory().get(0).getRefsKey().toString()
					+ ",106563604,106563610"
					+ ",'" + entity.getName().replaceAll("'", "''") + "'"
					+ ")";
	
			log.info("cmd: " + cmd);
			Query query = markerDAO.createNativeQuery(cmd);
			query.getResultList();

			// to update the mrk_location_cache table				
			try {
				log.info("processMarker/mrkLocationUtilities");
				mrklocationUtilities(String.valueOf(entity.get_marker_key()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
		// end mouse only stuff
		
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
		Boolean modified = true;
		Boolean setStrainNeedsReview = false;
		
		log.info("processMarker/update");

		// only if marker symbol has been changed
		if (!domain.getSymbol().equals(entity.getSymbol())) {
			setStrainNeedsReview = true;
		}
		
		entity.setMarkerType(markerTypeDAO.get(Integer.valueOf(domain.getMarkerTypeKey())));	
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		
		// note:  entity.setOrganism() is ignored on purpose

		entity.setChromosome(domain.getChromosome().toUpperCase());			
		if (domain.getChromosome().equals("UN")) {
			entity.setCmOffset(-999.0);
		}			

		if (domain.getCytogeneticOffset() == null || domain.getCytogeneticOffset().isEmpty()) {
			entity.setCytogeneticOffset(null);
		}
		else {
			entity.setCytogeneticOffset(domain.getCytogeneticOffset());
		}

		if (domain.getCmOffset() == null || domain.getCmOffset().isEmpty()) {
			entity.setCmOffset(null);
		}
		else {
			entity.setCmOffset(Double.valueOf(domain.getCmOffset()));
		}

		// process marker accession ids that can be edited
		if (domain.getEditAccessionIds() != null && !domain.getEditAccessionIds().isEmpty()) {
			if (accessionService.process(domain.getMarkerKey(), domain.getEditAccessionIds(), mgiTypeName, user)) {
				modified = true;
			}
		}
		
		// mouse only stuff		
		log.info("processMarker/getOrganism");		
		if (domain.getOrganismKey().equals("1")) {			
			log.info("processMarker/mouse only stuff");

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
		
			// process all notes
			if (noteService.process(domain.getMarkerKey(), domain.getEditorNote(), mgiTypeKey, user)) {
				modified = true;
			}
			if (noteService.process(domain.getMarkerKey(), domain.getSequenceNote(), mgiTypeKey, user)) {
				modified = true;	
			}
			if (noteService.process(domain.getMarkerKey(), domain.getRevisionNote(), mgiTypeKey, user)) {
				modified = true;	
			}
			if (noteService.process(domain.getMarkerKey(), domain.getStrainNote(), mgiTypeKey, user)) {
				modified = true;
			}
			if (noteService.process(domain.getMarkerKey(), domain.getLocationNote(), mgiTypeKey, user)) {
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
	
			// process feature types
			// use qualifier 'Generic Annotation Qualifier', value = null
			if (domain.getFeatureTypes() != null && !domain.getFeatureTypes().isEmpty()) {
				if (domain.getFeatureTypes().get(0).getTermKey() != null && !domain.getFeatureTypes().get(0).getTermKey().isEmpty()) {
					if (annotationService.processMarkerFeatureType(domain.getMarkerKey(), 
							domain.getFeatureTypes(), 
							domain.getFeatureTypes().get(0).getAnnotTypeKey(),
							Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user) == true) {
						modified = true;			
					}
				}
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

		if (setStrainNeedsReview == true) {
			String cmd;
			Query query;
			
		    cmd = "select count(*) from PRB_setStrainReview (" + domain.getMarkerKey() + ",NULL)";
		    log.info("cmd: " + cmd);
		    query = markerDAO.createNativeQuery(cmd);
		    query.getResultList();	
		}
		
		// return entity translated to domain
		log.info("processMarker/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processMarker/update/returned results succsssful");
		return results;
	}

	@Transactional
	public MarkerDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MarkerDomain domain = new MarkerDomain();
		if (markerDAO.get(key) != null) {
			domain = translator.translate(markerDAO.get(key));
		}
		markerDAO.clear();
		return domain;
	}

	@Transactional
	public SearchResults<MarkerDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		results.setItem(translator.translate(markerDAO.get(key)));
		markerDAO.clear();
		return results;
	}
	
	@Transactional
	public SearchResults<MarkerDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		Marker entity = markerDAO.get(key);
		results.setItem(translator.translate(markerDAO.get(key)));
		markerDAO.remove(entity);
		return results;
	}

	@Transactional	
	public SearchResults<MarkerDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<MarkerDomain> results = new SearchResults<MarkerDomain>();
		String cmd = "select count(*) as objectCount from mrk_marker where _organism_key = 1";
		
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
	public List<SlimMarkerDomain> search(MarkerDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		String cmd = "";
		String select = "select distinct m._marker_key, m._marker_type_key, m.symbol, left(m.symbol, 1), substring(m.symbol, '\\d+')::int";
		String from = "from mrk_marker m";
		String where = "where m._organism_key";
		String orderBy = "order by m._marker_type_key, left(m.symbol, 1), substring(m.symbol, '\\d+')::int NULLS FIRST, m.symbol";
		String limit = Constants.SEARCH_RETURN_LIMIT;
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

		// if parameter exists, then add to where-clause
		if (searchDomain.getOrganismKey() == null) {
			where = where + " is not null";
		}
		else {
			where = where + " = " + searchDomain.getOrganismKey();			
		}
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("m", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			value = searchDomain.getSymbol().replaceAll("'", "''");
			where = where + "\nand m.symbol ilike '" + value + "'" ;
		}
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			value = searchDomain.getName().replaceAll("'", "''");
			where = where + "\nand m.name ilike '" + value + "'" ;
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
		if (searchDomain.getEditorNote() != null  && !searchDomain.getEditorNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getEditorNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note1._notetype_key = 1004 and note1.note ilike '" + value + "'" ;
			from_editorNote = true;
		}
		if (searchDomain.getSequenceNote() != null  && !searchDomain.getSequenceNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getSequenceNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note2._notetype_key = 1009 and note2.note ilike '" + value + "'" ;
			from_sequenceNote = true;
		}
		if (searchDomain.getRevisionNote() != null  && !searchDomain.getRevisionNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getRevisionNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note3._notetype_key = 1030 and note3.note ilike '" + value + "'" ;
			from_revisionNote = true;
		}
		if (searchDomain.getStrainNote() != null  && !searchDomain.getStrainNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getStrainNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note4._notetype_key = 1035 and note4.note ilike '" + value + "'" ;
			from_strainNote = true;
		}
		if (searchDomain.getLocationNote() != null  && !searchDomain.getLocationNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getLocationNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note5._notetype_key = 1049 and note5.note ilike '" + value + "'" ;
			from_locationNote = true;
		}
	
		// marker accession id
		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().get(0).getAccID().isEmpty()) {
			if (!searchDomain.getMgiAccessionIds().get(0).getAccID().startsWith("MGI:")) {
				where = where + "\nand a.numericPart = '" + searchDomain.getMgiAccessionIds().get(0).getAccID() + "'";
			}
			else {
				where = where + "\nand a.accID = '" + searchDomain.getMgiAccessionIds().get(0).getAccID().toUpperCase() + "'";
			}			
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
				where = where + "\nand mh._Refs_key = " + searchDomain.getHistory().get(0).getRefsKey();
				from_history = true;
			}
			else if (searchDomain.getHistory().get(0).getJnumid() != null && !searchDomain.getHistory().get(0).getJnumid().isEmpty()) {
				String jnumid = searchDomain.getHistory().get(0).getJnumid().toUpperCase();
				if (!jnumid.contains("J:")) {
					jnumid = "J:" + jnumid;
				}
				where = where + "\nand mh.jnumid = '" + jnumid + "'";
				from_history = true;
			}
			if (searchDomain.getHistory().get(0).getShort_citation() != null && !searchDomain.getHistory().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getHistory().get(0).getShort_citation().replace("'",  "''");
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
				value = searchDomain.getSynonyms().get(0).getSynonym().replaceAll("'", "''");
				where = where + "\nand ms.synonym ilike '" + value + "'";
				from_synonym = true;
			}
			if (searchDomain.getSynonyms().get(0).getRefsKey() != null && !searchDomain.getSynonyms().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand ms._Refs_key = " + searchDomain.getSynonyms().get(0).getRefsKey();
				from_synonym = true;
			}
			else if (searchDomain.getSynonyms().get(0).getJnumid() != null && !searchDomain.getSynonyms().get(0).getJnumid().isEmpty()) {
				String jnumid = searchDomain.getSynonyms().get(0).getJnumid().toUpperCase();
				if (!jnumid.contains("J:")) {
					jnumid = "J:" + jnumid;
				}
				where = where + "\nand ms.jnumid ilike '" + jnumid + "'";
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
			if (searchDomain.getRefAssocs().get(0).getRefAssocTypeKey() != null && !searchDomain.getRefAssocs().get(0).getRefAssocTypeKey().isEmpty()) {
				where = where + "\nand mr._refassoctype_key = " + searchDomain.getRefAssocs().get(0).getRefAssocTypeKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand mr._Refs_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			else if (searchDomain.getRefAssocs().get(0).getJnumid() != null && !searchDomain.getRefAssocs().get(0).getJnumid().isEmpty()) {
				String jnumid = searchDomain.getRefAssocs().get(0).getJnumid().toUpperCase();
				if (!jnumid.contains("J:")) {
					jnumid = "J:" + jnumid;
				}
				where = where + "\nand mr.jnumid ilike '" + jnumid + "'";
				from_reference = true;
			}				
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replace("'",  "''");
				where = where + "\nand mr.short_citation ilike '" + value + "'";
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
				else if (searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getJnumid() != null && !searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getJnumid().isEmpty()) {
					String jnumid = searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getJnumid().toUpperCase();
					if (!jnumid.contains("J:")) {
						jnumid = "J:" + jnumid;
					}
					where = where + "\nand acc1.jnumid ilike '" + jnumid + "'";
					from_editAccession = true;
				}
				if (searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getShort_citation() != null 
						&& !searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getShort_citation().isEmpty()) {
					value = searchDomain.getEditAccessionIds().get(0).getReferences().get(0).getShort_citation().replace("'",  "''");
					where = where + "\nand acc1.short_citation ilike '" + value + "'";
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
				else if (searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getJnumid() != null && !searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getJnumid().isEmpty()) {
					String jnumid = searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getJnumid().toUpperCase();
					if (!jnumid.contains("J:")) {
						jnumid = "J:" + jnumid;
					}
					where = where + "\nand acc2.jnumid ilike '" + jnumid + "'";
					from_noneditAccession = true;
				}
				if (searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getShort_citation() != null 
						&& !searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getShort_citation().isEmpty()) {
					value = searchDomain.getNonEditAccessionIds().get(0).getReferences().get(0).getShort_citation().replace("'",  "''");
					where = where + "\nand acc2.short_citation ilike '" + value + "'";
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
		if (searchDomain.getFeatureTypes() != null && !searchDomain.getFeatureTypes().get(0).getTermKey().isEmpty()) {
			where = where + "\nand v._term_key = " + searchDomain.getFeatureTypes().get(0).getTermKey();
			from_featureTypes = true;
		}
		
		if (searchDomain.getTssToGene() != null) {
			where = where + "\nand (tss1.marker1 ilike '" + searchDomain.getTssToGene().get(0).getSymbol() + "'" +
						"or tss1.marker2 ilike '" + searchDomain.getTssToGene().get(0).getSymbol() + "')";
			from_tss1 = true;
		}
		
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
			from = from + ", acc_accession acc1";
			where = where + "\nand m._marker_key = acc1._object_key";
			where = where + "\nand acc1._mgitype_key = 2";
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
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy  + "\n" + limit;
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
	public List<SlimMarkerDomain> getNextGmSequence() {
		// return next Gm symbol that is available in the sequence
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();
		
		String cmd = "\nselect 'Gm' || (max(substring(symbol from 3)::int) + 1) as nextSequence from mrk_marker where symbol ~ '^Gm[\\d]+$'";	
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {				
				SlimMarkerDomain domain = new SlimMarkerDomain();				
				domain.setSymbol(rs.getString("nextSequence"));				
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
	public List<SlimMarkerDomain> getNextRrSequence() {
		// return next Rr symbol that is available in the sequence
		
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();
		
		String cmd = "\nselect 'Rr' || (max(substring(symbol from 3)::int) + 1) as nextSequence from mrk_marker where symbol ~ '^Rr[\\d]+$'"
				+ "\nand substring(symbol from 3)::int <= 948";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {				
				SlimMarkerDomain domain = new SlimMarkerDomain();				
				domain.setSymbol(rs.getString("nextSequence"));				
				results.add(domain);			
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public String getMarkerByRefSQL (String accid, int offset, int limit, boolean returnCount) {
		// SQL for selecting marker by acc id
		
		String cmd;

		if (returnCount) {
			cmd = "\nselect count(_marker_key) as total_count from MRK_SummaryByReference_View where jnumid = '" + accid + "'";
			return cmd;
		}
		
		cmd = "\nselect * from MRK_SummaryByReference_View where jnumid = '" + accid + "'";
		cmd = addPaginationSQL(cmd, "markerstatus, symbol", offset, limit);

		return cmd;
	}

	@Transactional	
	public SearchResults<SummaryMarkerDomain> getMarkerByRef(String accid, int offset, int limit) {
		// return list of marker domains by reference jnum id

		SearchResults<SummaryMarkerDomain> results = new SearchResults<SummaryMarkerDomain>();
		List<SummaryMarkerDomain> summaryResults = new ArrayList<SummaryMarkerDomain>();
		
		String cmd = getMarkerByRefSQL(accid, offset, limit, true);
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getLong("total_count");
				results.offset = offset;
				results.limit = limit;
				markerDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		cmd = getMarkerByRefSQL(accid, offset, limit, false);
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryMarkerDomain domain = new SummaryMarkerDomain();
				domain.setJnumID(accid);
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));
				domain.setName(rs.getString("name"));
			    domain.setAccID(rs.getString("accid"));
				domain.setMarkerStatus(rs.getString("markerStatus"));
				domain.setMarkerType(rs.getString("markerType"));
				domain.setFeatureTypes(rs.getString("featureTypes"));
				domain.setSynonyms(rs.getString("synonyms"));	
				summaryResults.add(domain);
				markerDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		results.items = summaryResults;
		return results;
	}		

	public Response downloadMarkerByRef (String accid) {
		String cmd = getMarkerByRefSQL (accid, -1, -1, false);
		return download(cmd, getTsvFileName("getMarkerByRef", accid), new MarkerFormatter());
	}

	public static class MarkerFormatter implements TsvFormatter {
		public String format (ResultSet obj) {
			String[][] cols = {
                	{"Symbol", "symbol"},
                	{"Marker Status", "markerSTatus"},
                	{"MGI ID", "accid"},
                	{"Name", "name"},
                	{"Synonyms", "synonyms"},
                	{"Feature Type", "featureTypes"},
                	{"Marker Type", "markerType"},
			};
			return formatTsvHelper(obj, cols);
		}
	}	
	
	@Transactional	
	public MarkerDomain getSummaryLinks(MarkerDomain domain) {
		// return marker domains with summary info by marker key attached
		// called from MarkerController/get()
		
		String cmd = "\nselect m._marker_key," 
				+ "\ncase when exists (select 1 from all_allele s where m._marker_key = s._marker_key) then 1 else 0 end as hasAllele," 
				+ "\ncase when exists (select 1 from mrk_reference s where m._marker_key = s._marker_key) then 1 else 0 end as hasReference," 
				+ "\ncase when exists (select 1 from gxd_index s where m._marker_key = s._marker_key) then 1 else 0 end as hasGxdIndex," 
				+ "\ncase when exists (select 1 from gxd_assay s where m._marker_key = s._marker_key) then 1 else 0 end as hasGxdAssay," 
				+ "\ncase when exists (select 1 from gxd_expression s where m._marker_key = s._marker_key) then 1 else 0 end as hasGxdResult," 
				+ "\ncase when exists (select 1 from prb_marker s where m._marker_key = s._marker_key) then 1 else 0 end as hasProbe," 
				+ "\ncase when exists (select 1 from gxd_antibodymarker s where m._marker_key = s._marker_key) then 1 else 0 end as hasAntibody," 
				+ "\ncase when exists (select 1 from mld_expt_marker s where m._marker_key = s._marker_key) then 1 else 0 end as hasMapping,"
				+ "\ncase when exists (select 1 from seq_marker_cache s where m._marker_key = s._marker_key) then 1 else 0 end as hasSequence"
				+ "\nfrom mrk_marker m" 
				+ "\nwhere m._marker_key = " + domain.getMarkerKey();
		
		log.info(cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				domain.setHasAllele(rs.getBoolean("hasAllele"));
				domain.setHasAntibody(rs.getBoolean("hasAntibody"));
				domain.setHasGxdAssay(rs.getBoolean("hasGxdAssay"));
				domain.setHasGxdIndex(rs.getBoolean("hasGxdIndex"));
				domain.setHasGxdResult(rs.getBoolean("hasGxdResult"));
				domain.setHasMapping(rs.getBoolean("hasMapping"));
				domain.setHasProbe(rs.getBoolean("hasProbe"));
				domain.setHasReference(rs.getBoolean("hasReference"));
				domain.setHasSequence(rs.getBoolean("hasSequence"));
				markerDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return domain;
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

		String cmd = "\nselect m._marker_key, m.symbol, m.name, m.chromosome";
		String from = "from mrk_marker m";
		String where = "where m._organism_key = 1"
				+ "\nand lower(symbol) = '" + value.toLowerCase() + "'";

		Boolean hasAccID = false;
		
		// withdrawn/reserved symbols do not have accession ids
		if (allowWithdrawn == false && allowReserved == false) {
			cmd = cmd + ", a.accID";
			from = from + ", acc_accession a"; 
			where = where + "\nand m._marker_key = a._object_key"
					+ "\nand a._mgitype_key = 2"
					+ "\nand a._logicaldb_key = 1"
					+ "\nand a.preferred = 1";
			hasAccID = true;
		}

		if (allowWithdrawn == false) {
			where = where + "\nand m._marker_status_key not in (2)";
		}

		if (allowReserved == false) {
			where = where + "\nand m._marker_status_key not in (3)";
		}
		
		cmd = cmd + "\n" + from + "\n" + where;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimMarkerDomain domain = new SlimMarkerDomain();						
				domain.setMarkerKey(rs.getString("_marker_key"));
				domain.setSymbol(rs.getString("symbol"));
				domain.setName(rs.getString("name"));
				domain.setChromosome(rs.getString("chromosome"));
				
				if (hasAccID) {
					domain.setAccID(rs.getString("accID"));			}
				
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// if more than 1 result, then use *exact* case from value
		// if no match on exact case, then empty results should be returned
		if (results.size() > 1) {
			List<SlimMarkerDomain> newResults = new ArrayList<SlimMarkerDomain>();
			for (int i = 0; i < results.size(); i++) {
				if (results.get(i).getSymbol().equals(value)) {
					newResults.add(results.get(i));
					break;
				}
			}
			results = newResults;
		}
		
		return results;
	}	

	@Transactional	
	public List<SlimMarkerDomain> validateMarker(SlimMarkerDomain searchDomain) {
		// use SlimMarkerDomain to return list of validated markers
		// returns empty list of values if validation fails

		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();
		Boolean hasChromosome = false;
		
		String cmd = "";
		String select = "select distinct m._marker_key ";
		String from = "from mrk_marker m";
		String where = "where m._marker_status_key not in (2,3)";
		
		Boolean from_accession = false;

		if (searchDomain.getOrganismKey() != null && !searchDomain.getOrganismKey().isEmpty()) {
			where = where + "\nand m._Organism_key = " + searchDomain.getOrganismKey();
		}
		else {
			where = where + "\nand m._Organism_key = 1";
		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			where = where + "\nand lower(m.symbol) = '" + searchDomain.getSymbol().toLowerCase() + "'" ;
		}

		if (searchDomain.getChromosome() != null && !searchDomain.getChromosome().isEmpty()) {
			where = where + "\nand m.chromosome = '" + searchDomain.getChromosome() + "'" ;
			hasChromosome = true;
		}
	
		if (searchDomain.getMarkerStatusKey() != null && !searchDomain.getMarkerStatusKey().isEmpty()) {
			where = where + "\nand m._marker_status_key in (" + searchDomain.getMarkerStatusKey() + ")";
		}

		if (searchDomain.getMarkerTypeKey() != null && !searchDomain.getMarkerTypeKey().isEmpty()) {
			where = where + "\nand m._marker_type_key in (" + searchDomain.getMarkerTypeKey() + ")";
		}
		
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (searchDomain.getOrganismKey() == null || searchDomain.getOrganismKey().isEmpty()) {
				if (!mgiid.contains("MGI:")) {
					mgiid = "MGI:" + mgiid;
				}
			}
			else if (searchDomain.getOrganismKey().equals("1") && !mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}			
			where = where + "\nand lower(acc.accID) = '" + mgiid.toLowerCase() + "'";	
			from_accession = true;
		}

		if (from_accession == true) {
			from = from + ", mrk_acc_view acc";
			where = where + "\nand m._marker_key = acc._object_key"
					+ "\nand acc._mgitype_key = 2"
					+ "\nand acc.preferred = 1";		
			if (searchDomain.getOrganismKey() == null || searchDomain.getOrganismKey().isEmpty()) {
				where = where + "\nand acc._logicaldb_key = 1";
			}
			else if (searchDomain.getOrganismKey().equals("1")) {
				where = where + "\nand acc._logicaldb_key = 1";
			}			
		}
		
		cmd = "\n" + select + "\n" + from + "\n" + where;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimMarkerDomain slimdomain = new SlimMarkerDomain();
				slimdomain = slimtranslator.translate(markerDAO.get(rs.getInt("_marker_key")));				
				markerDAO.clear();
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// if more than 1 result and may contains chromosome, then use *exact* case from value
		// if no match on exact case, then empty results should be returned
		if (results.size() > 1) {
			List<SlimMarkerDomain> newResults = new ArrayList<SlimMarkerDomain>();
			for (int i = 0; i < results.size(); i++) {
				if (results.get(i).getSymbol().equals(searchDomain.getSymbol())) {
					newResults.add(results.get(i));
				}
			}
			// still > 1 result means need to check chromosome too
			if (newResults.size() > 1 && hasChromosome) {
				for (int i = 0; i < results.size(); i++) {
					if (results.get(i).getChromosome().equals(searchDomain.getChromosome())) {
						newResults.add(results.get(i));
						break;
					}
				}				
			}
			results = newResults;
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

		// if more than 1 result, then use *exact* case from value
		// if no match on exact case, then empty results should be returned
		if (listOfResults.size() > 1) {
			List<SlimMarkerOfficialChromDomain> newResults = new ArrayList<SlimMarkerOfficialChromDomain>();
			for (int i = 0; i < listOfResults.size(); i++) {
				if (listOfResults.get(i).getSymbol2().equals(searchDomain.getSymbol2())) {
					newResults.add(listOfResults.get(i));
					break;
				}
			}
			listOfResults = newResults;
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
			
				String termKey = terms.get(i).getTermKey();
								
				// 3:Cytogenetic Marker; the only valid terms are:
				//				   7196768 | chromosomal deletion
				//				   7196769 | insertion
				//				   7196770 | chromosomal inversion
				//				   7196771 | Robertsonian fusion
				//				   7196772 | reciprocal chromosomal translocation
				//				   7196773 | chromosomal translocation
				//				   7196774 | chromosomal duplication
				//				   7196775 | chromosomal transposition
				//				   7222413 | unclassified cytogenetic marker
				
				if (!markerTypeKey.equals("3")
						&& (termKey.equals("7196768")
						|| termKey.equals("7196774")
						|| termKey.equals("7196770")
						|| termKey.equals("7196773")
						|| termKey.equals("7196775")
						|| termKey.equals("7196769")
						|| termKey.equals("7196772")
						|| termKey.equals("7196771")
						|| termKey.equals("7222413"))) {
					validation = false;
				}
				else if (markerTypeKey.equals("3")
						&& !termKey.equals("7196768")
						&& !termKey.equals("7196774")
						&& !termKey.equals("7196770")
						&& !termKey.equals("7196773")
						&& !termKey.equals("7196775")
						&& !termKey.equals("7196769")
						&& !termKey.equals("7196772")
						&& !termKey.equals("7196771")
						&& !termKey.equals("7222413")) {
					validation = false;
				}				
				// 7:Pseudogene; the only valid terms are:
				//				   6967235 | pseudogenic gene segment
				//				   7288448 | pseudogenic region
				//				   7288449 | polymorphic pseudogene
				//				   7313348 | pseudogene
				else if (!markerTypeKey.equals("7") 
						&& (termKey.equals("7288449")
						|| termKey.equals("7313348")
						|| termKey.equals("6967235")
						|| termKey.equals("7288448"))) {
					validation = false;
				}
				else if (markerTypeKey.equals("7")
						&& !termKey.equals("7288449")
						&& !termKey.equals("7313348")
						&& !termKey.equals("6967235")
						&& !termKey.equals("7288448")) {
					validation = false;
				}		
				
				// 9:Other Genome Feature; the only valid terms are:
				//				   6238178 | other genome feature
				//				   7648966 | retrotransposon
				//				   7648967 | telomere
				//				   7648968 | minisatellite
				//				   7648969 | unclassified other genome feature
				//				   9272146 | endogenous retroviral region
				//				  11928467 | mutation defined region
				//				  15406205 | CpG island
				//				  15406207 | promoter
				//				  36700088 | TSS cluster
				//				  97015607 | enhancer
				//				  97015608 | promoter flanking region
				//				  97015609 | CTCF binding site
				//				  97015610 | transcription factor binding site
				//				  97015611 | open chromatin region
				// 				  103059155 | silencer
				//                103059156 | insulator
				//                103059157 | imprinting control region
				//                103059158 | locus control region
				//                103059159 | response element
				//                103059160 | histone modification
				//                103059161 | intronic regulatory region
				//                103059162 | splice enhancer
				//                103059163 | insulator binding site
				//				  113728573	| origin of replication
				//				  113728574	| transcriptional cis regulatory region
				
				else if (!markerTypeKey.equals("9")
						&& (termKey.equals("6238178")
							|| termKey.equals("15406205")
							|| termKey.equals("9272146")
							|| termKey.equals("7648968")
							|| termKey.equals("11928467")
							|| termKey.equals("15406207")
							|| termKey.equals("7648966")
							|| termKey.equals("7648967")
							|| termKey.equals("7648969")
							|| termKey.equals("36700088")							
                            || termKey.equals("97015607")
                            || termKey.equals("97015608")
                            || termKey.equals("97015609")
                            || termKey.equals("97015610")
                            || termKey.equals("97015611")
                            || termKey.equals("103059155")
                            || termKey.equals("103059156")
                            || termKey.equals("103059157")
                            || termKey.equals("103059158")
                            || termKey.equals("103059159")
                            || termKey.equals("103059160")
                            || termKey.equals("103059161")
                            || termKey.equals("103059162")                 												
                            || termKey.equals("103059163")
                            || termKey.equals("113728573")                          
                            || termKey.equals("113728574")                                                      
							)) {
					validation = false;
				}
				// 9:Other Genome Feature
				else if (markerTypeKey.equals("9")
						 && !termKey.equals("6238178")
						 && !termKey.equals("15406205")
						 && !termKey.equals("9272146")
						 && !termKey.equals("7648968")
						 && !termKey.equals("11928467")
						 && !termKey.equals("15406207")
						 && !termKey.equals("7648966")
						 && !termKey.equals("7648967")
						 && !termKey.equals("7648969")
						 && !termKey.equals("36700088")							
                         && !termKey.equals("97015607")
                         && !termKey.equals("97015608")
                         && !termKey.equals("97015609")
                         && !termKey.equals("97015610")
                         && !termKey.equals("97015611")
                         && !termKey.equals("103059155")
                         && !termKey.equals("103059156")
                         && !termKey.equals("103059157")
                         && !termKey.equals("103059158")
                         && !termKey.equals("103059159")
                         && !termKey.equals("103059160")
                         && !termKey.equals("103059161")
                         && !termKey.equals("103059162")
                         && !termKey.equals("103059163")
                         && !termKey.equals("113728573")                          
                         && !termKey.equals("113728574")                          
						 ) {
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
	public SearchResults<SlimMarkerDomain> eiUtilities(MarkerUtilitiesForm searchForm, User user) throws IOException, InterruptedException {
	
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
		
		// when we replace pgdbutilities/bin/ei/markerWithdrawal.py
		// with java version, then we will use "User user".
        
        // input:  searchForm parameters
		Map<String, Object> params = searchForm.getSearchFields();

        // output: results
		SearchResults<SlimMarkerDomain> results = new SearchResults<SlimMarkerDomain>();
		List<SlimMarkerDomain> listOfResults = new ArrayList<SlimMarkerDomain>();

		String runCmd = eiUtilitiesScript;
        runCmd = runCmd + " -S" + server;
        runCmd = runCmd + " -D" + db;
        runCmd = runCmd + " -U" + username;
        runCmd = runCmd + " -P" + passwordFile;
        runCmd = runCmd + " --eventKey=" + (String) params.get("eventKey");
        runCmd = runCmd + " --eventReasonKey=" + (String) params.get("eventReasonKey");
        runCmd = runCmd + " --oldKey=" + (String) params.get("oldKey");
        runCmd = runCmd + " --refKey=" + (String) params.get("refKey");
        runCmd = runCmd + " --addAsSynonym=" + (String) params.get("addAsSynonym");

		// rename/delete is "oldKey"
		String key = (String) params.get("oldKey");

		// mrk_event = rename
		if (params.get("eventKey").equals("106563605")) {
			runCmd = runCmd + " --newName=\"" + 
						((String) params.get("newName")).replaceAll("'",  "''") + "\"";
			runCmd = runCmd + " --newSymbols=\"" + (String) params.get("newSymbol") + "\"";
		}
		
		// mrk_event = merge
		if (params.get("eventKey").equals("106563606") || params.get("eventKey").equals("106563607")) {
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

		// pre-processing snapshot
		// once this is done, the pgdbutilities/bin/ei/markerWithdrawal* methods are obsolete
		
//		String queryCmd = "\nselect _marker_key, symbol from mrk_current_view"
//				+ "\nwhere _current_key = " + key
//				+ "\norder by symbol";	
//		log.info(queryCmd);
//		
//		try {
//			ResultSet rs = sqlExecutor.executeProto(queryCmd);
//			while (rs.next()) {				
//				SlimMarkerDomain domain = new SlimMarkerDomain();				
//				domain.setMarkerKey(rs.getString("_marker_key"));
//				domain.setSymbol(rs.getString("symbol"));				
//				listOfResults.add(domain);
//			}
//			sqlExecutor.cleanup();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		String cmd= "";
//		
//		// rename
//		if (params.get("eventKey").equals("2")) {
//			cmd = "select count(*) from MRK_simpleWithdrawal ("
//				+ user.get_user_key().intValue()
//				+ "," + (String) params.get("oldKey")
//				+ "," + (String) params.get("refKey")
//				+ "," + (String) params.get("eventReasonKey")
//				+ ",'" + (String) params.get("newSymbol") + "'"
//				+ ",'" + ((String) params.get("newName")).replaceAll("'",  "''") + "'"
//				+ "," + (String) params.get("addAsSynonym")				
//				+ ")";
//		}
//		
//		// merge
//		else if (params.get("eventKey").equals("3")) {
//			cmd = "select count(*) from MRK_mergeWithdrawal ("
//					+ user.get_user_key().intValue()
//					+ "," + (String) params.get("oldKey")
//					+ "," + (String) params.get("newKey")					
//					+ "," + (String) params.get("refKey")
//					+ "," + (String) params.get("eventKey")					
//					+ "," + (String) params.get("eventReasonKey")
//					+ "," + (String) params.get("addAsSynonym")				
//					+ ")";			
//		}
//		
//		//allele of
//		else if (params.get("eventKey").equals("4")) {
//			cmd = "select count(*) from MRK_alleleWithdrawal ("
//					+ user.get_user_key().intValue()
//					+ "," + (String) params.get("oldKey")
//					+ "," + (String) params.get("newKey")					
//					+ "," + (String) params.get("refKey")
//					+ "," + (String) params.get("eventReasonKey")
//					+ "," + (String) params.get("addAsSynonym")	
//					+ ")";			
//		}
//		
//		// delete
//		else if (params.get("eventKey").equals("6")) {
//			cmd = "select count(*) from MRK_deleteWithdrawal ("
//					+ user.get_user_key().intValue()
//					+ "," + (String) params.get("oldKey")
//					+ "," + (String) params.get("refKey")
//					+ "," + (String) params.get("eventReasonKey")
//					+ ")";			
//		}
//
//		log.info("cmd: " + cmd);
//		Query query = markerDAO.createNativeQuery(cmd);
//		query.getResultList();
		
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
	
	@Transactional		
	public Boolean mrklocationUtilities(String markerKey) throws IOException, InterruptedException {
		// see mrkcacheload/mrklocation.py

        // input:  markerKey

        // output: true/false
        Boolean returnCode = false;
        
		String runCmd = mrkLocationUtilitiesScript;
        runCmd = runCmd + " " + markerKey;
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			returnCode = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);
			returnCode = false;
		}			
		
		return returnCode;
	}

	@Transactional		
	public Boolean mrkmcvUtilities(String markerKey) throws IOException, InterruptedException {
		// see mrkcacheload/mrkmcv.py
        
        // input:  markerKey

        // output: true/false
        Boolean returnCode = false;
        
		String runCmd = mrkmcvUtilitiesScript;
        runCmd = runCmd + " -S" + server;
        runCmd = runCmd + " -D" + db;
        runCmd = runCmd + " -U" + username;
        runCmd = runCmd + " -P" + passwordFile;
		runCmd = runCmd + " -K" + markerKey;
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			returnCode = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);
			returnCode = false;
		}			
		
		return returnCode;
	}
	
	@Transactional		
	public Boolean mrkrefByMarkerUtilities(String markerKey) throws IOException, InterruptedException {
		// see mrkcacheload/mrkrefByMarker.py

        // input:  markerKey

        // output: true/false
        Boolean returnCode = false;
        
		String runCmd = mrkrefByMarkerUtilitiesScript;
        runCmd = runCmd + " " + markerKey;
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			returnCode = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);
			returnCode = false;
		}			
		
		return returnCode;
	}	

	@Transactional		
	public Boolean mrkrefByReferenceUtilities(String refsKey) throws IOException, InterruptedException {
		// see mrkcacheload/mrkrefByReference.py

        // input:  refsKey

        // output: true/false
        Boolean returnCode = false;
        
		String runCmd = mrkrefByReferenceUtilitiesScript;
        runCmd = runCmd + " " + refsKey;
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			returnCode = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);
			returnCode = false;
		}			
		
		return returnCode;
	}

}
