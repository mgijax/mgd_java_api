package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.DenormGenotypeAnnotDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeAnnotDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeAlleleReferenceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDODomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeMPDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeAnnotTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGenotypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationDAO;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationHeaderDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GenotypeAnnotService extends BaseService<DenormGenotypeAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private AnnotationDAO annotationDAO;
	@Inject
	private AnnotationService annotationService;
	@Inject
	private AnnotationHeaderDAO annotationHeaderDAO;
	
	private GenotypeAnnotTranslator translator = new GenotypeAnnotTranslator();
	private SlimGenotypeTranslator slimtranslator = new SlimGenotypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "12";

	@Transactional
	public SearchResults<DenormGenotypeAnnotDomain> create(DenormGenotypeAnnotDomain domain, User user) {	
		log.info("GenotypeAnnotService.create");
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<DenormGenotypeAnnotDomain> update(DenormGenotypeAnnotDomain domain, User user) {
		// translate pwi/incoming denormalized json domain to list of normalized domain (GenotypeAnnotDomain)
		// use normalized domain to process hibernate entities
		
		log.info("GenotypeAnnotService.update");
		
		GenotypeAnnotDomain genoAnnotDomain = new GenotypeAnnotDomain();
		List<AnnotationDomain> annotList = new ArrayList<AnnotationDomain>();

		// assuming the pwi will always pass in the annotTypeKey
		String annotTypeKey = domain.getAnnots().get(0).getAnnotTypeKey();
		
    	genoAnnotDomain.setGenotypeKey(domain.getGenotypeKey());
		//genoAnnotDomain.setGenotypeDisplay(domain.getGenotypeDisplay());
    	
    	if (annotTypeKey.equals("1002")) {
    		genoAnnotDomain.setHeaders(domain.getHeaders());
    	}
    	genoAnnotDomain.setAllowEditTerm(domain.getAllowEditTerm());
		
    	// Iterate thru incoming denormalized GenotypeAnnot domain
		for (int i = 0; i < domain.getAnnots().size(); i++) {
			
			// if processStatus == "x", then continue; no need to create domain/process anything
			if (domain.getAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
				continue;
			}
			
			// if term and qualifier are empty
			if (domain.getAnnots().get(i).getTermKey().isEmpty()
					&& domain.getAnnots().get(i).getQualifierKey().isEmpty()) {
				continue;
			}
			
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
			
			log.info("GenotypeAnnotService.update : denormAnnotDomain to annotDomain");
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
			log.info("GenotypeAnnotService.update : add evidence list");           
			EvidenceDomain evidenceDomain = new EvidenceDomain();
            List<EvidenceDomain> evidenceList = new ArrayList<EvidenceDomain>();
            evidenceDomain.setProcessStatus(denormAnnotDomain.getProcessStatus());
            
            // if term or qualifier has been changed...
            if (denormAnnotDomain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("GenotypeAnnotService.update : check for changes");
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
			
            // Only MP has properties
            if (annotTypeKey.equals("1002")) {
				// sex-specificity : create evidence-property list of 1 result
    			log.info("GenotypeAnnotService.update : evidence property");           
				EvidencePropertyDomain evidencePropertyDomain = new EvidencePropertyDomain();			
				List<EvidencePropertyDomain > evidencePropertyList = new ArrayList<EvidencePropertyDomain>();
				evidencePropertyDomain.setProcessStatus(denormAnnotDomain.getProcessStatus());
				evidencePropertyDomain.setEvidencePropertyKey(denormAnnotDomain.getProperties().get(0).getEvidencePropertyKey());
				evidencePropertyDomain.setPropertyTermKey(denormAnnotDomain.getProperties().get(0).getPropertyTermKey());
				evidencePropertyDomain.setStanza(denormAnnotDomain.getProperties().get(0).getStanza());
				evidencePropertyDomain.setSequenceNum(denormAnnotDomain.getProperties().get(0).getSequenceNum());		
				evidencePropertyDomain.setValue(denormAnnotDomain.getProperties().get(0).getValue());
				evidencePropertyList.add(evidencePropertyDomain);
				evidenceDomain.setProperties(evidencePropertyList);
            }
            
			// add evidenceDomain to evidenceList
			evidenceList.add(evidenceDomain);

			// add evidenceList to annotDomain
			annotDomain.setEvidence(evidenceList);
            
			// add annotDomain to annotList
			annotList.add(annotDomain);         
		}
		
		// add annotList to the genoAnnotDomain and process annotations
		if (annotList.size() > 0) {
			log.info("send json normalized domain to services");
			genoAnnotDomain.setAnnots(annotList);
			annotationService.process(genoAnnotDomain.getAnnots(), user);
		}
		
		// update headerList regardless of changes to rest of annotations
		// MP annotations : 1002
		if (annotTypeKey.equals("1002")) {				
			// process change to annotationHeaderDomian.getSequenceNum()
			List<AnnotationHeaderDomain> headerList = genoAnnotDomain.getHeaders();
		
			// change order of existing headers
			if (headerList != null && !headerList.isEmpty()) {
				for (int j = 0; j < headerList.size(); j++) {
					AnnotationHeaderDomain annotationHeaderDomain = headerList.get(j);
					AnnotationHeader annotationHeaderEntity = annotationHeaderDAO.get(Integer.valueOf(annotationHeaderDomain.getAnnotHeaderKey()));
					if (annotationHeaderDomain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
						annotationHeaderEntity.setSequenceNum(annotationHeaderDomain.getSequenceNum());
						annotationHeaderEntity.setModification_date(new Date());
						annotationHeaderEntity.setModifiedBy(user);
						annotationHeaderEntity.setApproval_date(new Date());
						annotationHeaderEntity.setApprovedBy(user);						
						annotationHeaderDAO.update(annotationHeaderEntity);
					}	
				}						
			}
			
			// process headers
			String cmd = "select count(*) from VOC_processAnnotHeader (" + user.get_user_key() + ","
					+ annotTypeKey + "," + genoAnnotDomain.getGenotypeKey() + ")";
			log.info("processGenotype/process header: " + cmd);
			Query query = genotypeDAO.createNativeQuery(cmd);
			query.getResultList();							
		}
			
		log.info("repackage incoming domain as results");		
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results = getResults(Integer.valueOf(domain.getGenotypeKey()));
		results.setItem(domain);
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public SearchResults<DenormGenotypeAnnotDomain> delete(Integer key, User user) {
		log.info("GenotypeAnnotService.delete");
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	public DenormGenotypeAnnotDomain get(Integer key) {
		//not implemented, but have to return something
		return new DenormGenotypeAnnotDomain();
	}

	@Transactional
	public DenormGenotypeAnnotDomain get(Integer key, Integer annotTypeKey) {
    	// get the DAO/entity and translate -> domain -> denormalized genotype annot domain
		
		// This is the annotType we want to process
		String controllerAnnotTypeKey = String.valueOf(annotTypeKey);
		
		DenormGenotypeAnnotDomain denormGenoAnnotDomain = new DenormGenotypeAnnotDomain();
		
    	try {
        	GenotypeAnnotDomain genoAnnotDomain = new GenotypeAnnotDomain();  
        	genoAnnotDomain = translator.translate(genotypeDAO.get(key));
        	genotypeDAO.clear();
        	//log.info("From the translator first annotKey: " + genoAnnotDomain.getAnnots().get(0).getAnnotKey());
			
			List<DenormAnnotationDomain> annotList = new ArrayList<DenormAnnotationDomain>();
			
			denormGenoAnnotDomain.setGenotypeKey(genoAnnotDomain.getGenotypeKey());
			//log.info("setting genotypeKey: " + genoAnnotDomain.getGenotypeKey() );

			denormGenoAnnotDomain.setGenotypeDisplay(genoAnnotDomain.getGenotypeDisplay());
			//log.info("setting genotypeDisplay: " + genoAnnotDomain.getGenotypeDisplay() );

			denormGenoAnnotDomain.setAccID(genoAnnotDomain.getAccID());
			//log.info("setting accid: " + genoAnnotDomain.getAccID());

			// set header if MP only
			if(controllerAnnotTypeKey.equals("1002")) {
			    denormGenoAnnotDomain.setHeaders(genoAnnotDomain.getHeaders());
			    //log.info("Adding MP annot");   
			}
			
			if (genoAnnotDomain.getAnnots() != null) {

				for (int i = 0; i < genoAnnotDomain.getAnnots().size(); i++) {	

					// annotation (term, qualifier)
					AnnotationDomain annotDomain = genoAnnotDomain.getAnnots().get(i);
					String domainAnnotTypeKey = annotDomain.getAnnotTypeKey();

					if (!domainAnnotTypeKey.equals(controllerAnnotTypeKey)) {
						//log.info("Skipping MP annot domainAnnotTypeKey: " + domainAnnotTypeKey + "annotKey: " + annotDomain.getAnnotKey() + " controllerAnnotTypeKey: " + controllerAnnotTypeKey);
						continue;
					}
					//log.info("Match. domainAnnotTypeKey: " + domainAnnotTypeKey + " controllerAnnotTypeKey: " + controllerAnnotTypeKey);

					// evidence
					for (int j = 0; j < genoAnnotDomain.getAnnots().get(i).getEvidence().size(); j++) {
						// annotation (term, qualifier)
						DenormAnnotationDomain denormAnnotDomain = new DenormAnnotationDomain();
						denormAnnotDomain.setProcessStatus(annotDomain.getProcessStatus());
	                    denormAnnotDomain.setAnnotKey(annotDomain.getAnnotKey());
	                    denormAnnotDomain.setAnnotTypeKey(annotDomain.getAnnotTypeKey());
	                    denormAnnotDomain.setAnnotType(annotDomain.getAnnotType());
	                    denormAnnotDomain.setObjectKey(annotDomain.getObjectKey());
	                    denormAnnotDomain.setTermKey(annotDomain.getTermKey());
	                    denormAnnotDomain.setTerm(annotDomain.getTerm());

	                    // get MP or DO depending on 
	                    if (controllerAnnotTypeKey.equals("1002")) {
	                    	denormAnnotDomain.setTermid(annotDomain.getMpIds().get(0).getAccID());
	                    	//log.info("Adding MP IDs: " + annotDomain.getMpIds().get(0).getAccID());
	                    }
	                    else if (controllerAnnotTypeKey.equals("1020")) {
	                    	denormAnnotDomain.setTermid(annotDomain.getDoIds().get(0).getAccID());
	                    	//log.info("Adding DO IDs: " + annotDomain.getDoIds().get(0).getAccID());
	                    }
	                    
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

						// evidence-property
						// properties are Lists - even though we have only one for MP (1002)(sex-specificity) 
						// No properties for DO (1020)
						if (domainAnnotTypeKey.equals("1002")) {
							denormAnnotDomain.setProperties(evidenceDomain.getProperties());
						}
						
						annotList.add(denormAnnotDomain);
					}
				}
			}

			// if MP annnot/1002, then check for duplicates
			if (annotTypeKey.equals(1002)) {
				annotList = checkMPDuplicates(key, annotTypeKey, annotList);
			}
			
			// if DO annnot/1020, then check for duplicates
			if (annotTypeKey.equals(1020)) {
				annotList = checkDODuplicates(key, annotTypeKey, annotList);
			}	
			
			// add List of annotation domains to the denormalized geno annot domain
			denormGenoAnnotDomain.setAnnots(annotList);
			
			// sort by jnum, term		
			Comparator<DenormAnnotationDomain> compareByJnum = Comparator.comparingInt(DenormAnnotationDomain::getJnum);			 
			Comparator<DenormAnnotationDomain> compareByTerm = Comparator.comparing(DenormAnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER);			 
			Comparator<DenormAnnotationDomain> compareAll = compareByJnum.thenComparing(compareByTerm);
			Collections.sort(annotList, compareAll);			

    	}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return denormGenoAnnotDomain;
	}

	@Transactional
	public List<DenormAnnotationDomain> checkMPDuplicates(Integer key, Integer annotTypeKey, List<DenormAnnotationDomain> annotList) {
		
		String cmd;
		List<SlimGenotypeMPDomain> duplicateList = new ArrayList<SlimGenotypeMPDomain>();

		cmd = "\nselect v._term_key, v._qualifier_key, e._evidenceterm_key, e._refs_key, p.value"
				+ "\nfrom VOC_Annot v, VOC_Evidence e, VOC_Evidence_Property p"
				+ "\nwhere v._AnnotType_key = 1002"
				+ "\nand v._annot_key = e._annot_key"
				+ "\nand e._annotevidence_key = p._annotevidence_key"
				+ "\nand p._propertyterm_key = 8836535"
				+ "\nand v._object_key = " + key
				+ "\ngroup by v._object_key, e._refs_key, v._term_key, v._qualifier_key, e._evidenceterm_key, p.value"
				+ "\nhaving count(*) > 1";
			
		log.info("check if MP annotations contains duplicates");			
		log.info(cmd);
			
		try {
				ResultSet rs = sqlExecutor.executeProto(cmd);
				while (rs.next()) {
					SlimGenotypeMPDomain domain = new SlimGenotypeMPDomain();
					domain.setTermKey(rs.getString("_term_key"));
					domain.setQualifierKey(rs.getString("_qualifier_key"));
					domain.setEvidenceTermKey(rs.getString("_evidenceterm_key"));
					domain.setRefsKey(rs.getString("_refs_key"));
					domain.setSexSpecificityValue(rs.getString("value"));
					duplicateList.add(domain);						
				}
				sqlExecutor.cleanup();
		}
		catch (Exception e) {
				e.printStackTrace();
		}
				
		if (duplicateList.size() > 0) {
			log.info("found MP annotaiton duplicates: " + duplicateList.size());
								
			for (int i = 0; i < annotList.size(); i++) {							
				// find result in annotList and set isDuplicate = true
				for (int j = 0; j < duplicateList.size(); j++) {															
					if (duplicateList.get(j).getTermKey().equals(annotList.get(i).getTermKey())
						&& duplicateList.get(j).getQualifierKey().equals(annotList.get(i).getQualifierKey())
						&& duplicateList.get(j).getEvidenceTermKey().equals(annotList.get(i).getEvidenceTermKey())
						&& duplicateList.get(j).getRefsKey().equals(annotList.get(i).getRefsKey())
						&& duplicateList.get(j).getSexSpecificityValue().equals(annotList.get(i).getProperties().get(0).getValue())) {
						log.info("found dup: " + annotList.get(i).getAnnotKey());
						annotList.get(i).setIsDuplicate(true);
					}
				}
			}
		}
		
		return annotList;
	}

	@Transactional
	public List<DenormAnnotationDomain> checkDODuplicates(Integer key, Integer annotTypeKey, List<DenormAnnotationDomain> annotList) {
		
		String cmd;
		List<SlimGenotypeDODomain> duplicateList = new ArrayList<SlimGenotypeDODomain>();

		cmd = "\nselect v._term_key, v._qualifier_key, e._evidenceterm_key, e._refs_key"
				+ "\nfrom VOC_Annot v, VOC_Evidence e"
				+ "\nwhere v._AnnotType_key = 1020"
				+ "\nand v._annot_key = e._annot_key"
				+ "\nand v._object_key = " + key
				+ "\ngroup by v._object_key, e._refs_key, v._term_key, v._qualifier_key, e._evidenceterm_key"
				+ "\nhaving count(*) > 1";
			
		log.info("check if DO annotations contains duplicates");			
		log.info(cmd);
			
		try {
				ResultSet rs = sqlExecutor.executeProto(cmd);
				while (rs.next()) {
					SlimGenotypeDODomain domain = new SlimGenotypeDODomain();
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
	public SearchResults<DenormGenotypeAnnotDomain> getResults(Integer key) {
		// get the denormalized domain -> results
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results.setItem(get(key));
		return results;
	}
		
	@Transactional	
	public SearchResults<DenormGenotypeAnnotDomain> getObjectCount(String annotType) {
		// return the object count from the database
		
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
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
	public List<SlimGenotypeDomain> search(DenormGenotypeAnnotDomain domain) {
		// using domain fields, generate SQL command
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 

		// sc - 10/4/19 removed the 'order by description' as we can't do that using
		// select "distinct on" because the description is arbitrary
		//String select = "select distinct on (v._object_key) v._object_key, v.description";
		// requirement changed/group description by _object_key
		// saving this SQL in case it is needed again
	
		String cmd = "";
		String select = "select distinct v._object_key, v.subtype, v.short_description";
		String from = "from gxd_genotype_summary_view v";		
		String where = "where v._mgitype_key = " + mgiTypeKey;
		String orderBy = "order by v._object_key, v.subtype, v.short_description";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		
		String value;

		Boolean from_annot = false;
		Boolean from_evidence = false;
		Boolean from_property = false;
		Boolean from_note = false;
		Boolean executeQuery = false;
	
		// if parameter exists, then add to where-clause
		
//		if (domain.getGenotypeKey() != null && !domain.getGenotypeKey().isEmpty()) {
//			where = where + "\nand v._object_key = " + domain.getGenotypeKey();
//		}
		
		if (domain.getGenotypeDisplay() != null && !domain.getGenotypeDisplay().isEmpty()) {
			where = where + "\nand v.description ilike '" + domain.getGenotypeDisplay() + "'";		
			executeQuery = true;
		}
		
		if (domain.getAccID() != null && !domain.getAccID().isEmpty()) {
			String mgiid = domain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(v.accID) = '" + mgiid.toLowerCase() + "'";
			executeQuery = true;
		}

		if (domain.getAnnots() != null) {
						
			DenormAnnotationDomain annotDomain = domain.getAnnots().get(0);
		
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
			
			// for MP annotations only
			if (domain.getAnnots().get(0).getAnnotTypeKey().equals("1002") && annotDomain.getProperties() != null && !annotDomain.getProperties().isEmpty()) {
				value = annotDomain.getProperties().get(0).getValue(); 
				if (value != null && !value.isEmpty()) {
					where = where + "\nand p._propertyterm_key = " + annotDomain.getProperties().get(0).getPropertyTermKey();
					where = where + "\nand p.value ilike '" + value + "'";
					from_evidence = true;
					from_property = true;
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
			
			if (annotDomain.getAllNotes() != null) {
				
				value = annotDomain.getAllNotes().get(0).getNoteChunk();
				if (value != null && !value.isEmpty()) {
					
					if (annotDomain.getAllNotes().get(0).getNoteTypeKey() != null 
							&& !annotDomain.getAllNotes().get(0).getNoteTypeKey().isEmpty()) {
						where = where + "\nand n._notetype_key = " + annotDomain.getAllNotes().get(0).getNoteTypeKey();
					}
					
					where = where + "\nand n.note ilike '" + value + "'";
					from_note = true;
				}
								
			}
		}
		
		// dependencies
		if (from_note == true) {
			from_evidence = true;
		}
		if (from_evidence == true) {
			from_annot = true;
		}
		
		// from/where construction
		if (from_annot == true) {
			from = from + ", voc_annot va";
			where = where + "\nand v._object_key = va._object_key" 
					+ "\nand v._logicaldb_key = 1"
					+ "\nand v.preferred = 1"
					+ "\nand va._annottype_key = " + domain.getAnnots().get(0).getAnnotTypeKey();
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
		if (from_note == true) {
			from = from + ", mgi_note_vocevidence_view n";
			where = where + "\nand e._annotevidence_key = n._object_key";
			executeQuery = true;
		}

		if (executeQuery == false) {
			log.info("executeQuery = false; not enough parameters in search");
			return results;
		}
		
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		// must match GenotypeService/search()		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			Integer prevObjectKey = 0;
			Integer newObjectKey = 0;
			String newDescription = "";
			String prevDescription = "";
			String newStrain = "";
			String prevStrain = "";
			Boolean addResults = false;
			
			// concatenate description when grouped by _object_key
			
			while (rs.next()) {
				
				newObjectKey = rs.getInt("_object_key");
				newStrain = rs.getString("subtype");
				newDescription = rs.getString("short_description");
								
				// group description by _object_key
				if (prevObjectKey.equals(0)) {
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				else if (newObjectKey.equals(prevObjectKey)) {
					prevDescription = prevDescription + "," + newDescription;
					addResults = false;
				}
				else {
					addResults = true;
				}
				
				if (addResults) {

					prevDescription = prevStrain + " " + prevDescription;
	
					SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
					slimdomain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					slimdomain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(slimdomain);
					
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				
				// if last record, then add to result set
				if (rs.isLast() == true) {
					
					if (prevObjectKey.equals(newObjectKey)) {
						if (prevDescription == null) {
							prevDescription = prevStrain;
						}
						else {
							prevDescription = prevStrain + " " + prevDescription;							
						}
					}
					else {
						prevObjectKey = newObjectKey;
						prevStrain = newStrain;
						prevDescription = newDescription;
						prevDescription = prevStrain + " " + prevDescription;
					}
					
					SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
					slimdomain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					slimdomain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(slimdomain);
				}
								
			}
			sqlExecutor.cleanup();
			
			// order by description - see note above at first 'select = '
			results.sort(Comparator.comparing(SlimGenotypeDomain::getGenotypeDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@Transactional	
	public List<SlimGenotypeDomain> searchByKeys(SlimGenotypeDomain domain) {
		// using domain fields, generate SQL command
		//
		// where domain.genotypeKey is a list of genotypeKey:
		//		1111,2222,3333,...
		
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();
	
		String cmd = "select distinct v._object_key, v.subtype, v.short_description"
			+ "\nfrom gxd_genotype_summary_view v"
			+ "\nwhere v._mgitype_key = " + mgiTypeKey 
			+  "\nand v._object_key in (" + domain.getGenotypeKey() + ")"
			+  "\norder by v._object_key, v.subtype, v.short_description";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			Integer prevObjectKey = 0;
			Integer newObjectKey = 0;
			String newDescription = "";
			String prevDescription = "";
			String newStrain = "";
			String prevStrain = "";
			Boolean addResults = false;
			
			// concatenate description when grouped by _object_key
			
			while (rs.next()) {
				
				newObjectKey = rs.getInt("_object_key");
				newStrain = rs.getString("subtype");
				newDescription = rs.getString("short_description");
								
				// group description by _object_key
				if (prevObjectKey.equals(0)) {
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				else if (newObjectKey.equals(prevObjectKey)) {
					prevDescription = prevDescription + "," + newDescription;
					addResults = false;
				}
				else {
					addResults = true;
				}
				
				if (addResults) {

					if (prevDescription == null) {
						prevDescription = prevStrain;
					}
					else {
						prevDescription = prevStrain + " " + prevDescription;
					}
					
					SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
					slimdomain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					slimdomain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(slimdomain);
					
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				
				// if last record, then add to result set
				if (rs.isLast() == true) {
					
					if (prevObjectKey.equals(newObjectKey)) {
						if (prevDescription == null) {
							prevDescription = prevStrain;
						}
						else {
							prevDescription = prevStrain + " " + prevDescription;
						}
					}
					else {
						prevObjectKey = newObjectKey;
						prevStrain = newStrain;
						prevDescription = newDescription;
						
						if (prevDescription == null) {
							prevDescription = prevStrain;
						}
						else {
							prevDescription = prevStrain + " " + prevDescription;
						}
					}
					
					SlimGenotypeDomain slimdomain = new SlimGenotypeDomain();
					slimdomain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					slimdomain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(slimdomain);
				}
								
			}
			sqlExecutor.cleanup();

			// order by description - see note above at first 'select = '
			results.sort(Comparator.comparing(SlimGenotypeDomain::getGenotypeDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@Transactional	
	public List<MGIReferenceAssocDomain> validateAlleleReference(SlimGenotypeAlleleReferenceDomain searchDomain) {
		// return a list of Allele/Reference associations that do not exist for this Genotype/Reference
		// returns empty list of values if validation fails

		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();
		
		String cmd = "\nselect distinct g._allele_key"
				+ "\nfrom GXD_AlleleGenotype g, ALL_Allele a" 
				+ "\nwhere g._Allele_key = a._Allele_key"
				+ "\nand a.isWildType = 0"
				+ "\nand g._Genotype_key = " + searchDomain.getGenotypeKey()
				+ "\nand not exists (select 1 from MGI_Reference_Assoc a where a._MGIType_key = 11" 
					+ "\nand a._Object_key = g._Allele_key"
					+ "\nand a._Refs_key = " + searchDomain.getRefsKey() + ")";
		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
				domain.setProcessStatus(Constants.PROCESS_CREATE);
				domain.setObjectKey(rs.getString("_allele_key"));
				domain.setMgiTypeKey("11");
				domain.setRefAssocType("Used-FC");
				domain.setRefsKey(searchDomain.getRefsKey());
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
