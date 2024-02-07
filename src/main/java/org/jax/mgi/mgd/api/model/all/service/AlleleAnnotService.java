package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.domain.DenormAlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDODomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleAnnotTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleAnnotTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class AlleleAnnotService extends BaseService<DenormAlleleAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AlleleDAO alleleDAO;
	@Inject
	private AnnotationDAO annotationDAO;
	@Inject
	private AnnotationService annotationService;
	
	private AlleleAnnotTranslator translator = new AlleleAnnotTranslator();
	private SlimAlleleAnnotTranslator slim1translator = new SlimAlleleAnnotTranslator();
	private SlimAlleleTranslator slim2translator = new SlimAlleleTranslator();

	private String mgiTypeKey = "11";

	@Transactional
	public SearchResults<DenormAlleleAnnotDomain> create(DenormAlleleAnnotDomain domain, User user) {	
		log.info("AlleleAnnotService.create");
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<DenormAlleleAnnotDomain> update(DenormAlleleAnnotDomain domain, User user) {
		// translate pwi/incoming denormalized json domain to list of normalized domain (AlleleAnnotDomain)
		// use normalized domain to process hibernate entities
		
		log.info("AlleleAnnotService.update");
		
		AlleleAnnotDomain alleleAnnotDomain = new AlleleAnnotDomain();
		List<AnnotationDomain> annotList = new ArrayList<AnnotationDomain>();

		// assuming the pwi will always pass in the annotTypeKey
		
		alleleAnnotDomain.setAlleleKey(domain.getAlleleKey());
    	
    	alleleAnnotDomain.setAllowEditTerm(domain.getAllowEditTerm());
		
    	// Iterate thru incoming denormalized alleleAnnot domain
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
            evidenceDomain.setRefsKey(denormAnnotDomain.getRefsKey());
            evidenceDomain.setCreatedByKey(denormAnnotDomain.getCreatedByKey());
            evidenceDomain.setModifiedByKey(denormAnnotDomain.getModifiedByKey());
            evidenceDomain.setAllNotes(denormAnnotDomain.getAllNotes());
			
			// add evidenceDomain to evidenceList
			evidenceList.add(evidenceDomain);

			// add evidenceList to annotDomain
			annotDomain.setEvidence(evidenceList);
            
			// add annotDomain to annotList
			annotList.add(annotDomain);         
		}
		
		// add annotList to the AlleleAnnotDomain and process annotations
		if (annotList.size() > 0) {
			log.info("send json normalized domain to services");			
			alleleAnnotDomain.setAnnots(annotList);
			annotationService.process(alleleAnnotDomain.getAnnots(), user);
		}
		
		log.info("repackage incoming domain as results");		
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results = getResults(Integer.valueOf(domain.getAlleleKey()));
		results.setItem(domain);
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public SearchResults<DenormAlleleAnnotDomain> delete(Integer key, User user) {
		log.info("AlleleAnnotService.delete");
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	public DenormAlleleAnnotDomain get(Integer key) {
		//not implemented, but have to return something
		return new DenormAlleleAnnotDomain();
	}

	@Transactional
	public DenormAlleleAnnotDomain get(Integer key, Integer annotTypeKey) {
    	// get the DAO/entity and translate -> domain -> denormalized allele annot domain
		
		// This is the annotType we want to process
		String controllerAnnotTypeKey = String.valueOf(annotTypeKey);
		
		DenormAlleleAnnotDomain denormAlleleAnnotDomain = new DenormAlleleAnnotDomain();

    	try {
        	AlleleAnnotDomain alleleAnnotDomain = new AlleleAnnotDomain();  
        	alleleAnnotDomain = translator.translate(alleleDAO.get(key));
        	alleleDAO.clear();
        	//log.info("From the translator first annotKey: " + alleleAnnotDomain.getAnnots().get(0).getAnnotKey());
			
			List<DenormAnnotationDomain> annotList = new ArrayList<DenormAnnotationDomain>();
			
			denormAlleleAnnotDomain.setAlleleKey(alleleAnnotDomain.getAlleleKey());
			log.info("setting alleleKey: " + alleleAnnotDomain.getAlleleKey() );
	
			denormAlleleAnnotDomain.setAccID(alleleAnnotDomain.getAccID());
			log.info("setting accid: " + alleleAnnotDomain.getAccID());
			
			if (alleleAnnotDomain.getAnnots() != null) {
				for (int i = 0; i < alleleAnnotDomain.getAnnots().size(); i++) {	
					
					// annotation (term, qualifier)
					AnnotationDomain annotDomain = alleleAnnotDomain.getAnnots().get(i);
					String domainAnnotTypeKey = annotDomain.getAnnotTypeKey();
					if (!domainAnnotTypeKey.equals(controllerAnnotTypeKey)) {
						log.info("Skipping annot domainAnnotTypeKey: " + domainAnnotTypeKey + "annotKey: " + annotDomain.getAnnotKey() + " controllerAnnotTypeKey: " + controllerAnnotTypeKey);
						continue;
					}
					log.info("Match domainAnnotTypeKey: " + domainAnnotTypeKey + " controllerAnnotTypeKey: " + controllerAnnotTypeKey);
					
					// evidence
					for (int j = 0; j < alleleAnnotDomain.getAnnots().get(i).getEvidence().size(); j++) {
	
						// annotation (term, qualifier)
						DenormAnnotationDomain denormAnnotDomain = new DenormAnnotationDomain();
						denormAnnotDomain.setProcessStatus(annotDomain.getProcessStatus());
	                    denormAnnotDomain.setAnnotKey(annotDomain.getAnnotKey());
	                    denormAnnotDomain.setAnnotTypeKey(annotDomain.getAnnotTypeKey());
	                    denormAnnotDomain.setAnnotType(annotDomain.getAnnotType());
	                    denormAnnotDomain.setObjectKey(annotDomain.getObjectKey());
	                    denormAnnotDomain.setTermKey(annotDomain.getTermKey());
	                    denormAnnotDomain.setTerm(annotDomain.getTerm());
	                    
	                    //if (controllerAnnotTypeKey.equals("1021")) {
	                    denormAnnotDomain.setTermid(annotDomain.getDoIds().get(0).getAccID());
	                    log.info("Adding Allele DO IDs: " + annotDomain.getDoIds().get(0).getAccID());
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
						denormAnnotDomain.setAllNotes(evidenceDomain.getAllNotes());

						// no properties for Allele/DO annots
						
						annotList.add(denormAnnotDomain);
					}
				}
			}	

			// if DO annnot/1021, then check for duplicates
			if (annotTypeKey.equals(1021)) {
				annotList = checkDODuplicates(key, annotTypeKey, annotList);
			}	
			
			// add List of annotation domains to the denormalized allele annot domain
			denormAlleleAnnotDomain.setAnnots(annotList);
			
			// sort by term, jnum
			annotList.sort(Comparator.comparing(DenormAnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER).thenComparingInt(DenormAnnotationDomain::getJnum));
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return denormAlleleAnnotDomain;
	}

	@Transactional
	public List<DenormAnnotationDomain> checkDODuplicates(Integer key, Integer annotTypeKey, List<DenormAnnotationDomain> annotList) {
		
		String cmd;
		List<SlimAlleleDODomain> duplicateList = new ArrayList<SlimAlleleDODomain>();

		cmd = "\nselect v._term_key, v._qualifier_key, e._evidenceterm_key, e._refs_key"
				+ "\nfrom VOC_Annot v, VOC_Evidence e"
				+ "\nwhere v._AnnotType_key = 1021"
				+ "\nand v._annot_key = e._annot_key"
				+ "\nand v._object_key = " + key
				+ "\ngroup by v._object_key, e._refs_key, v._term_key, v._qualifier_key, e._evidenceterm_key"
				+ "\nhaving count(*) > 1";
			
		log.info("check if DO annotations contains duplicates");			
		log.info(cmd);
			
		try {
				ResultSet rs = sqlExecutor.executeProto(cmd);
				while (rs.next()) {
					SlimAlleleDODomain domain = new SlimAlleleDODomain();
					domain.setTermKey(rs.getString("_term_key"));
					domain.setQualifierKey(rs.getString("_qualifier_key"));
					domain.setEvidenceTermKey(rs.getString("_evidenceterm_key"));
					domain.setRefsKey(rs.getString("_refs_key"));
					duplicateList.add(domain);						
				}
				sqlExecutor.cleanup();
		}
		catch (Exception e) {
				e.printStackTrace();
		}
				
		if (duplicateList.size() > 0) {
			log.info("found DO annotaiton duplicates: " + duplicateList.size());
								
			for (int i = 0; i < annotList.size(); i++) {							
				// find result in annotList and set isDuplicate = true
				for (int j = 0; j < duplicateList.size(); j++) {															
					if (duplicateList.get(j).getTermKey().equals(annotList.get(i).getTermKey())
						&& duplicateList.get(j).getQualifierKey().equals(annotList.get(i).getQualifierKey())
						&& duplicateList.get(j).getEvidenceTermKey().equals(annotList.get(i).getEvidenceTermKey())
						&& duplicateList.get(j).getRefsKey().equals(annotList.get(i).getRefsKey())) {
						log.info("found dup: " + annotList.get(i).getAnnotKey());
						annotList.get(i).setIsDuplicate(true);
					}
				}
			}
		}
		
		return annotList;
	}
	
	@Transactional
	public SearchResults<DenormAlleleAnnotDomain> getResults(Integer key) {
		// get the denormalized domain -> results
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results.setItem(get(key));
		return results;
	}
		
	@Transactional	
	public SearchResults<DenormAlleleAnnotDomain> getObjectCount(String annotType) {
		// return the object count from the database
		
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
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
	public List<SlimAlleleAnnotDomain> search(DenormAlleleAnnotDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimAlleleAnnotDomain> results = new ArrayList<SlimAlleleAnnotDomain>();

		String cmd = "";
		String select = "select distinct v._object_key, v.description";
		String from = "from all_summary_view v";		
		String where = "where v._mgitype_key = " + mgiTypeKey;
		String orderBy = "order by v._object_key, v.description";
		
		String value;

		Boolean from_accession = false;
		Boolean from_annot = false;
		Boolean from_evidence = false;
		Boolean executeQuery = false;
		
		// if parameter exists, then add to where-clause
		
//		if (searchDomain.getAlleleKey() != null && !searchDomain.getAlleleKey().isEmpty()) {
//			where = where + "\nand v._object_key = " + searchDomain.getAlleleKey();
//		}
		
		if (searchDomain.getAlleleDisplay() != null && !searchDomain.getAlleleDisplay().isEmpty()) {
			where = where + "\nand v.description ilike '" + searchDomain.getAlleleDisplay().replace("'",  "''") + "'";
			executeQuery = true;
		}
		
		// accession id
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {
			if (!searchDomain.getAccID().startsWith("MGI:")) {
				where = where + "\nand a.numericPart = '" + searchDomain.getAccID() + "'";
			}
			else {
				where = where + "\nand a.accID = '" + searchDomain.getAccID().toUpperCase() + "'";
			}
			from_accession = true;
			executeQuery = true;
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
		}
		
		if (from_evidence == true) {
			from_annot = true;
		}
		
		// from/where construction
		if (from_accession == true) {
			from = from + ", all_acc_view a";
			where = where + "\nand v._object_key = a._object_key" 
					+ "\nand a._mgitype_key = " + mgiTypeKey;
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
		
		if (executeQuery == false) {
			log.info("executeQuery = false; not enough parameters in search");
			return results;
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info("searchCmd: " + cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimAlleleAnnotDomain domain = new SlimAlleleAnnotDomain();
				domain = slim1translator.translate(alleleDAO.get(rs.getInt("_object_key")));				
				domain.setAlleleDisplay(rs.getString("description"));
				alleleDAO.clear();				
				results.add(domain);					
			}
			sqlExecutor.cleanup();
			
			results.sort(Comparator.comparing(SlimAlleleAnnotDomain::getAlleleDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@Transactional	
	public List<SlimAlleleDomain> searchByKeys(SlimAlleleDomain searchDomain) {
		// using domain fields, generate SQL command
		//
		// where domain.alleleKey is a list of alleleKey:
		//		1111,2222,3333,...
		
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
	
		// ordering should match the alleleService.search(AlleleDomain searchDomain)
		String cmd = "select distinct v._object_key, v1.sequenceNum, a.symbol as description"
			+ "\nfrom all_summary_view v, all_allele a, voc_term v1"
			+ "\nwhere v._mgitype_key = " + mgiTypeKey 
			+ "\nand v._object_key = a._allele_key"
			+ "\nand a._allele_status_key = v1._term_key"		
			+  "\nand v._object_key in (" + searchDomain.getAlleleKey() + ")"
			+  "\norder by v1.sequenceNum, description";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
						
			while (rs.next())  {
				SlimAlleleDomain domain = new SlimAlleleDomain();
				domain = slim2translator.translate(alleleDAO.get(rs.getInt("_object_key")));				
				domain.setAlleleDisplay(rs.getString("description"));
				alleleDAO.clear();				
				results.add(domain);					
			}
			sqlExecutor.cleanup();
			
			//results.sort(Comparator.comparing(SlimAlleleDomain::getAlleleDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
