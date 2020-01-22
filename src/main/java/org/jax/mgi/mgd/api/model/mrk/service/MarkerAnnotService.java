package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.GOTrackingDAO;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.DenormMarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerGOReferenceDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.GOTracking;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerAnnotTranslator;
import org.jax.mgi.mgd.api.model.mrk.translator.SlimMarkerAnnotTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerAnnotService extends BaseService<DenormMarkerAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private AnnotationDAO annotationDAO;
	@Inject
	private GOTrackingDAO goTrackingDAO;
	@Inject
	private AnnotationService annotationService;
	
	private MarkerAnnotTranslator translator = new MarkerAnnotTranslator();
	
	private SlimMarkerAnnotTranslator slimtranslator = new SlimMarkerAnnotTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "2";

	@Transactional
	public SearchResults<DenormMarkerAnnotDomain> create(DenormMarkerAnnotDomain domain, User user) {	
		log.info("MarkerAnnotService.create");
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<DenormMarkerAnnotDomain> update(DenormMarkerAnnotDomain domain, User user) {
		// translate pwi/incoming denormalized json domain to list of normalized domain (MarkerAnnotDomain)
		// use normalized domain to process hibernate entities
		
		log.info("MarkerAnnotService.update");
		
		MarkerAnnotDomain markerAnnotDomain = new MarkerAnnotDomain();
		List<AnnotationDomain> annotList = new ArrayList<AnnotationDomain>();

		// assuming the pwi will always pass in the annotTypeKey
		
		markerAnnotDomain.setMarkerKey(domain.getMarkerKey());
    	markerAnnotDomain.setAllowEditTerm(domain.getAllowEditTerm());
    	markerAnnotDomain.setGoNote(domain.getGoNote());
    	markerAnnotDomain.setGoTracking(domain.getGoTracking());

    	// Iterate thru incoming denormalized markerAnnot domain
		for (int i = 0; i < domain.getAnnots().size(); i++) {
			
			// if processStatus == "x", then continue; no need to create domain/process anything
			if (domain.getAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
				continue;
			}
			
			//log.info("domain index: " + i);
			
			DenormAnnotationDomain denormAnnotDomain = domain.getAnnots().get(i);
		
			// annotation (term, qualifier)
			AnnotationDomain annotDomain = new AnnotationDomain();
			
			//
			// if processStatus == "d", then process as "u"
			// 1 annotation may have >= 1 evidence
			// 1 evidence may be a "d", but other evidences may be "x", "u" or "c"
			//
			if (domain.getAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				annotDomain.setProcessStatus(Constants.PROCESS_UPDATE);
			}
			else {
				annotDomain.setProcessStatus(denormAnnotDomain.getProcessStatus());
			}
					
            annotDomain.setAnnotKey(denormAnnotDomain.getAnnotKey());
            annotDomain.setAnnotTypeKey(denormAnnotDomain.getAnnotTypeKey());
            annotDomain.setAnnotType(denormAnnotDomain.getAnnotType());
            annotDomain.setObjectKey(denormAnnotDomain.getObjectKey());
            annotDomain.setTermKey(denormAnnotDomain.getTermKey());
            annotDomain.setTerm(denormAnnotDomain.getTerm());           
            annotDomain.setQualifierKey(denormAnnotDomain.getQualifierKey());
            annotDomain.setQualifierAbbreviation(denormAnnotDomain.getQualifierAbbreviation());
            annotDomain.setQualifier(denormAnnotDomain.getQualifier());
            annotDomain.setAllowEditTerm(domain.getAllowEditTerm());
            
            // evidence : create evidence list of 1 result
            //log.info("add evidence list");
			EvidenceDomain evidenceDomain = new EvidenceDomain();
            List<EvidenceDomain> evidenceList = new ArrayList<EvidenceDomain>();
            evidenceDomain.setProcessStatus(denormAnnotDomain.getProcessStatus());
                        
            // if term or qualifier has been changed...
            if (denormAnnotDomain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				//log.info("GenotypeAnnotService.update : check for changes");
            	Annotation entity = annotationDAO.get(Integer.valueOf(denormAnnotDomain.getAnnotKey()));
				if (!denormAnnotDomain.getTermKey().equals(String.valueOf(entity.getTerm().get_term_key()))
	            		|| !denormAnnotDomain.getQualifierKey().equals(String.valueOf(entity.getQualifier().get_term_key()))) {
	            	annotDomain.setProcessStatus(Constants.PROCESS_SPLIT); 
	            	evidenceDomain.setProcessStatus(Constants.PROCESS_SPLIT);           	
	            }				
            }
            
            evidenceDomain.setAnnotEvidenceKey(denormAnnotDomain.getAnnotEvidenceKey());
            evidenceDomain.setEvidenceTermKey(denormAnnotDomain.getEvidenceTermKey());
            evidenceDomain.setInferredFrom(denormAnnotDomain.getInferredFrom());
            evidenceDomain.setRefsKey(denormAnnotDomain.getRefsKey());
            evidenceDomain.setCreatedByKey(denormAnnotDomain.getCreatedByKey());
            evidenceDomain.setModifiedByKey(denormAnnotDomain.getModifiedByKey());
            evidenceDomain.setProperties(denormAnnotDomain.getProperties());
			
			// add evidenceDomain to evidenceList
			evidenceList.add(evidenceDomain);

			// add evidenceList to annotDomain
			annotDomain.setEvidence(evidenceList);
            
			// add annotDomain to annotList
			annotList.add(annotDomain);         
		}
		
		// add annotList to the MarkerAnnotDomain and process annotations
		if (annotList.size() > 0) {
			log.info("send json normalized domain to services");			
			markerAnnotDomain.setAnnots(annotList);
			annotationService.process(markerAnnotDomain.getAnnots(), user);
			
			// go-tracking/updating 
			if (domain.getGoTracking().get(0).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				try {
					String newCompletionStr = domain.getGoTracking().get(0).getCompletion_date();
					Date newCompletion = new Date();
					
					GOTracking goTrackingEntity = new GOTracking();
					goTrackingEntity.set_marker_key(Integer.valueOf(domain.getMarkerKey()));
					
					if (newCompletionStr != null && !newCompletionStr.isEmpty()) {						
						newCompletion = new SimpleDateFormat("dd/MM/yyyy").parse(newCompletionStr);
					}
					else {
						newCompletion = null;
					}
					
					goTrackingEntity.setCompletedBy(user);
					goTrackingEntity.setCompletion_date(newCompletion);	
					goTrackingDAO.update(goTrackingEntity);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		log.info("repackage incoming domain as results");		
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results = getResults(Integer.valueOf(domain.getMarkerKey()));
		results.setItem(domain);
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public SearchResults<DenormMarkerAnnotDomain> delete(Integer key, User user) {
		log.info("MarkerAnnotService.delete");
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	public DenormMarkerAnnotDomain get(Integer key) {
		//not implemented, but have to return something
		return new DenormMarkerAnnotDomain();
	}

	@Transactional
	public DenormMarkerAnnotDomain get(Integer key, Integer annotTypeKey) {
    	// get the DAO/entity and translate -> domain -> denormalized marker annot domain
		
		// This is the annotType to process
		String controllerAnnotTypeKey = String.valueOf(annotTypeKey);
		
		DenormMarkerAnnotDomain denormMarkerAnnotDomain = new DenormMarkerAnnotDomain();

    	try {
        	MarkerAnnotDomain markerAnnotDomain = new MarkerAnnotDomain();  
        	markerAnnotDomain = translator.translate(markerDAO.get(key));
        	markerDAO.clear();
			
			List<DenormAnnotationDomain> annotList = new ArrayList<DenormAnnotationDomain>();
			
			denormMarkerAnnotDomain.setMarkerKey(markerAnnotDomain.getMarkerKey());
			denormMarkerAnnotDomain.setMarkerDisplay(markerAnnotDomain.getMarkerDisplay());
			denormMarkerAnnotDomain.setMarkerStatusKey(markerAnnotDomain.getMarkerStatusKey());			
			denormMarkerAnnotDomain.setMarkerStatus(markerAnnotDomain.getMarkerStatus());				
			denormMarkerAnnotDomain.setMarkerTypeKey(markerAnnotDomain.getMarkerTypeKey());			
			denormMarkerAnnotDomain.setMarkerType(markerAnnotDomain.getMarkerType());			
			denormMarkerAnnotDomain.setAccID(markerAnnotDomain.getAccID());
			denormMarkerAnnotDomain.setGoNote(markerAnnotDomain.getGoNote());
			denormMarkerAnnotDomain.setGoTracking(markerAnnotDomain.getGoTracking());

			if (markerAnnotDomain.getAnnots() != null) {
				for (int i = 0; i < markerAnnotDomain.getAnnots().size(); i++) {	
					
					// annotation (term, qualifier)
					AnnotationDomain annotDomain = markerAnnotDomain.getAnnots().get(i);
					String domainAnnotTypeKey = annotDomain.getAnnotTypeKey();
					if (!domainAnnotTypeKey.equals(controllerAnnotTypeKey)) {
						continue;
					}
										
					// evidence
					for (int j = 0; j < markerAnnotDomain.getAnnots().get(i).getEvidence().size(); j++) {
	
						// annotation (term, qualifier)
						DenormAnnotationDomain denormAnnotDomain = new DenormAnnotationDomain();
						denormAnnotDomain.setProcessStatus(annotDomain.getProcessStatus());
	                    denormAnnotDomain.setAnnotKey(annotDomain.getAnnotKey());
	                    denormAnnotDomain.setAnnotTypeKey(annotDomain.getAnnotTypeKey());
	                    denormAnnotDomain.setAnnotType(annotDomain.getAnnotType());
	                    denormAnnotDomain.setObjectKey(annotDomain.getObjectKey());
	                    denormAnnotDomain.setTermKey(annotDomain.getTermKey());
	                    denormAnnotDomain.setTerm(annotDomain.getTerm());

	                    //if (controllerAnnotTypeKey.equals("1000")) {
	                    denormAnnotDomain.setTermid(annotDomain.getGoIds().get(0).getAccID());
	                    denormAnnotDomain.setGoDagAbbrev(annotDomain.getGoDagAbbrev());
	                    //}
	                    
	                    denormAnnotDomain.setQualifierKey(annotDomain.getQualifierKey());
		                denormAnnotDomain.setQualifierAbbreviation(annotDomain.getQualifierAbbreviation());
		                denormAnnotDomain.setQualifier(annotDomain.getQualifier());
		
		                // evidence
						EvidenceDomain evidenceDomain = annotDomain.getEvidence().get(j);
		                denormAnnotDomain.setAnnotEvidenceKey(evidenceDomain.getAnnotEvidenceKey());
		                denormAnnotDomain.setEvidenceTermKey(evidenceDomain.getEvidenceTermKey());
		                denormAnnotDomain.setEvidenceTerm(evidenceDomain.getEvidenceTerm());
		                denormAnnotDomain.setEvidenceAbbreviation(evidenceDomain.getEvidenceAbbreviation());
		                denormAnnotDomain.setInferredFrom(evidenceDomain.getInferredFrom());		               
		                denormAnnotDomain.setRefsKey(evidenceDomain.getRefsKey());
		                denormAnnotDomain.setJnumid(evidenceDomain.getJnumid());
		                denormAnnotDomain.setJnum(Integer.valueOf(evidenceDomain.getJnum()));
		                denormAnnotDomain.setShort_citation(evidenceDomain.getShort_citation());
		                denormAnnotDomain.setCreatedByKey(evidenceDomain.getCreatedByKey());
		                denormAnnotDomain.setCreatedBy(evidenceDomain.getCreatedBy());
		                denormAnnotDomain.setModifiedByKey(evidenceDomain.getModifiedByKey());
		                denormAnnotDomain.setModifiedBy(evidenceDomain.getModifiedBy());
						denormAnnotDomain.setCreation_date(evidenceDomain.getCreation_date());
						denormAnnotDomain.setModification_date(evidenceDomain.getModification_date());
						
						List<EvidencePropertyDomain> propertyDomain = evidenceDomain.getProperties();
						denormAnnotDomain.setProperties(propertyDomain);
						if (propertyDomain != null) {
							denormAnnotDomain.setHasProperties(true);
						}
						
						annotList.add(denormAnnotDomain);
					}
				}
			}	
		
			// sort by goDagAbbrev, modification_date desc, term							
			orderByA(annotList);
			
			// add List of annotation domains to the denormalized marker annot domain
			denormMarkerAnnotDomain.setAnnots(annotList);
			
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return denormMarkerAnnotDomain;
	}

	@Transactional
	public void orderByA(List<DenormAnnotationDomain> annotList) {
		// order by dag abbrev, modification date desc, term			
		Comparator<DenormAnnotationDomain> compareByAbbrev = Comparator.comparing(DenormAnnotationDomain::getGoDagAbbrev);	
		Comparator<DenormAnnotationDomain> compareByModificationDate = Comparator.comparing(DenormAnnotationDomain::getModification_date).reversed();			 
		Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm);			 
		Comparator<DenormAnnotationDomain> compareAll = compareByAbbrev.thenComparing(compareByModificationDate).thenComparing(compareByTerm);
		Collections.sort(annotList, compareAll);		
	}

	@Transactional
	public void orderByB(List<DenormAnnotationDomain> annotList) {
		// order by creation date desc, term			
		Comparator<DenormAnnotationDomain> compareByCreationDate = Comparator.comparing(DenormAnnotationDomain::getCreation_date).reversed();			 
		Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm);			 
		Comparator<DenormAnnotationDomain> compareAll = compareByCreationDate.thenComparing(compareByTerm);
		Collections.sort(annotList, compareAll);		
	}
	
	@Transactional
	public void orderByC(List<DenormAnnotationDomain> annotList) {
		// order by term id, term			
		Comparator<DenormAnnotationDomain> compareByTermId = Comparator.comparing(DenormAnnotationDomain::getTermid);			 
		Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm);			 
		Comparator<DenormAnnotationDomain> compareAll = compareByTermId.thenComparing(compareByTerm);
		Collections.sort(annotList, compareAll);		
	}

	@Transactional
	public void orderByD(List<DenormAnnotationDomain> annotList) {
		// order by jnum,term			
		Comparator<DenormAnnotationDomain> compareByJnum = Comparator.comparingInt(DenormAnnotationDomain::getJnum);			 
		Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm);			 
		Comparator<DenormAnnotationDomain> compareAll = compareByJnum.thenComparing(compareByTerm);
		Collections.sort(annotList, compareAll);		
	}

	@Transactional
	public void orderByE(List<DenormAnnotationDomain> annotList) {
		// order by evidence term, term			
		Comparator<DenormAnnotationDomain> compareByEvidenceTerm = Comparator.comparing(DenormAnnotationDomain::getEvidenceTerm);			 
		Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm);			 
		Comparator<DenormAnnotationDomain> compareAll = compareByEvidenceTerm.thenComparing(compareByTerm);
		Collections.sort(annotList, compareAll);		
	}

	@Transactional
	public void orderByF(List<DenormAnnotationDomain> annotList) {
		// order by modification date desc, term			
		Comparator<DenormAnnotationDomain> compareByModificationDate = Comparator.comparing(DenormAnnotationDomain::getModification_date).reversed();			 
		Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm);			 
		Comparator<DenormAnnotationDomain> compareAll = compareByModificationDate.thenComparing(compareByTerm);
		Collections.sort(annotList, compareAll);		
	}

	@Transactional
	public SearchResults<DenormAnnotationDomain> getOrderBy(DenormMarkerAnnotDomain domain) {
		// return ordered annotList

		Integer orderBy = domain.getOrderBy();
		SearchResults<DenormAnnotationDomain> results = new SearchResults<DenormAnnotationDomain>();
		List<DenormAnnotationDomain> annotList = new ArrayList<DenormAnnotationDomain>();	
			
		// skip new rows; no DAG abbreviations
		for (int i = 0; i < domain.getAnnots().size(); i++) {
			if (!domain.getAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				annotList.add(domain.getAnnots().get(i));
			}
		}

		if (orderBy.equals(0)) {
			orderByA(annotList);
		}
		else if (orderBy.equals(1)) {
			orderByB(annotList);
		}
		else if (orderBy.equals(2)) {
			orderByC(annotList);
		}
		else if (orderBy.equals(3)) {
			orderByD(annotList);
		}
		else if (orderBy.equals(4)) {
			orderByE(annotList);
		}
		else if (orderBy.equals(5)) {
			orderByF(annotList);
		}		

		results.setItems(annotList);
		return results;
	}
	
	@Transactional
	public SearchResults<DenormAnnotationDomain> getOrderByF(List<DenormAnnotationDomain> annotList) {
		// return ordered annotList
		SearchResults<DenormAnnotationDomain> results = new SearchResults<DenormAnnotationDomain>();
		orderByF(annotList);
		results.setItems(annotList);
		return results;
	}
	
	@Transactional
	public SearchResults<DenormMarkerAnnotDomain> getResults(Integer key) {
		// get the denormalized domain -> results
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results.setItem(get(key));
		return results;
	}

	@Transactional	
	public SearchResults<DenormMarkerAnnotDomain> getObjectCount(String annotType) {
		// return the object count from the database
		
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		String cmd = "select count(distinct _object_key) as objectCount from voc_annot where _annottype_key = " + annotType;
		
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
	public List<SlimMarkerAnnotDomain> search(DenormMarkerAnnotDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimMarkerAnnotDomain> results = new ArrayList<SlimMarkerAnnotDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 

		String cmd = "";
		String select = "select distinct v._object_key, v.description";
		String from = "from mrk_summary_view v, mrk_marker m";		
		String where = "where v._mgitype_key = " + mgiTypeKey
				+ "\nand v._object_key = m._marker_key"
				+ "\nand m._marker_status_key = 1";
		String orderBy = "order by v._object_key, v.description";
		
		String value;

		Boolean from_accession = false;
		Boolean from_goNote = false;
		Boolean from_goTracking = false;
		Boolean from_annot = false;
		Boolean from_evidence = false;
		Boolean from_property = false;
		Boolean executeQuery = false;
		
		// if parameter exists, then add to where-clause
		
		if (searchDomain.getMarkerDisplay() != null && !searchDomain.getMarkerDisplay().isEmpty()) {
			where = where + "\nand v.short_description ilike '" + searchDomain.getMarkerDisplay() + "'";
			executeQuery = true;
		}
		
		// accession id
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(a.accID) = '" + mgiid.toLowerCase() + "'";
			from_accession = true;
			executeQuery = true;
		}

		if (searchDomain.getGoNote() != null  && !searchDomain.getGoNote().isEmpty()) {
			value = searchDomain.getGoNote().get(0).getNoteChunk().replace("'",  "''");
			if (value.length() > 0) {
				where = where + "\nand note._notetype_key = 1002 and note.note ilike '" + value + "'" ;
				from_goNote = true;
				executeQuery = true;
			}
		}

		// go tracking; is completed?
		if (searchDomain.getGoTracking() != null  && !searchDomain.getGoTracking().isEmpty()) {
			if (searchDomain.getGoTracking().get(0).getIsCompleted() != null) {				
				if (searchDomain.getGoTracking().get(0).getIsCompleted().equals(1)) {				
					where = where + "\nand trk.completion_date is not null";
				}
				else {
					where = where + "\nand trk.completion_date is null";
				}
				from_annot = true;
				from_goTracking = true;
				executeQuery = true;
			}
		}
		
		if (searchDomain.getAnnots() != null) {
						
			DenormAnnotationDomain annotDomain = searchDomain.getAnnots().get(0);
		
			value = annotDomain.getTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand va._term_key = " + value;
				from_annot = true;
			}
				
			value = annotDomain.getQualifierKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand va._qualifier_key = " + value;
				from_annot = true;
			}
			
			String cmResults[] = DateSQLQuery.queryByCreationModification("e", 
				annotDomain.getCreatedBy(), 
				annotDomain.getModifiedBy(), 
				annotDomain.getCreation_date(), 
				annotDomain.getModification_date());
	
			if (cmResults.length > 0) {
				if (cmResults[0].length() > 0 || cmResults[1].length() > 0) {
					from = from + cmResults[0];
					where = where + cmResults[1];
					from_evidence = true;
				}
			}

			value = annotDomain.getEvidenceTermKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand e._evidenceterm_key = " + value;
				from_evidence = true;			
			}
			
			value = annotDomain.getRefsKey();
			String jnumid = annotDomain.getJnumid();		
			if (value != null && !value.isEmpty()) {
				where = where + "\nand e._Refs_key = " + value;
				from_evidence = true;
			}
			else if (jnumid != null && !jnumid.isEmpty()) {
				jnumid = jnumid.toUpperCase();
				if (!jnumid.contains("J:")) {
						jnumid = "J:" + jnumid;
				}
				where = where + "\nand e.jnumid = '" + jnumid + "'";
				from_evidence = true;			
			}
			
			if (annotDomain.getProperties() != null) {

				value = String.valueOf(annotDomain.getProperties().get(0).getStanza());
				if (value != null && !value.isEmpty()) {
					if (Integer.valueOf(value) > 1) {
						where = where + "\nand p.stanza = " + value;
						from_property = true;
					}
				}
				
				value = annotDomain.getProperties().get(0).getPropertyTermKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p._propertyterm_key = " + value;
					from_property = true;
				}

				value = annotDomain.getProperties().get(0).getValue();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p.value ilike '" + value + "'";
					from_property = true;
				}
				
			}
			
		}

		if (from_property == true) {
			from_evidence = true;
		}
		if (from_evidence == true) {
			from_annot = true;
		}
		
		if (from_accession == true) {
			from = from + ", mrk_acc_view a";
			where = where + "\nand v._object_key = a._object_key" 
					+ "\nand a._mgitype_key = " + mgiTypeKey;
			executeQuery = true;
		}
		if (from_goNote == true) {
			from = from + ", mgi_note_marker_view note";
			where = where + "\nand v._marker_key = note._object_key";
			executeQuery = true;			
		}
		if (from_goTracking == true) {
			from = from + ", go_tracking trk";
			where = where + "\nand v._object_key = trk._marker_key";
			executeQuery = true;			
		}		
		if (from_annot == true) {
			from = from + ", voc_annot va";
			where = where + "\nand v._object_key = va._object_key" 
					+ "\nand v._logicaldb_key = 1"
					+ "\nand v.preferred = 1"
					+ "\nand va._annottype_key = " + searchDomain.getAnnots().get(0).getAnnotTypeKey();
			executeQuery = true;
		}
		if (from_evidence == true) {
			from = from + ", voc_evidence e";
			where = where + "\nand va._annot_key = e._annot_key";
			executeQuery = true;
		}
		if (from_property == true) {
			from = from + ", voc_evidence_property p";
			where = where + "\nand e._annotevidence_key = p._annotevidence_key";
			executeQuery = true;
		}
		
		if (executeQuery == false) {
			log.info("executeQuery = false; not enough parameters in search");
			return results;
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info("searchCmd: " + cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimMarkerAnnotDomain domain = new SlimMarkerAnnotDomain();
				domain = slimtranslator.translate(markerDAO.get(rs.getInt("_object_key")));				
				domain.setMarkerDisplay(rs.getString("description"));
				markerDAO.clear();				
				results.add(domain);					
			}
			sqlExecutor.cleanup();
			
			results.sort(Comparator.comparing(SlimMarkerAnnotDomain::getMarkerDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@Transactional	
	public List<MarkerGOReferenceDomain> getGOReferences(Integer key) {
		// search references by marker key 
		// return MarkerGOReferenceDomain
		
		List<MarkerGOReferenceDomain> results = new ArrayList<MarkerGOReferenceDomain>();

		String cmd = "select r._Refs_key, jnum, jnumid, short_citation"
				+ "\nfrom BIB_GOXRef_View r"
				+ "\nwhere r._Marker_key = " + key 
				+ "\nand not exists (select 1 from VOC_Annot a, VOC_Evidence e"
				+ "\nwhere a._Annot_key = e._Annot_key"
				+ "\nand e._Refs_key = r._Refs_key"
				+ "\nand a._AnnotType_key = 1000)"
				+ "\norder by r.jnum desc"; 

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerGOReferenceDomain domain = new MarkerGOReferenceDomain();
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setJnum(rs.getString("jnum"));
				domain.setJnumid(rs.getString("jnumid"));
				domain.setShort_citation(rs.getString("short_citation"));
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
