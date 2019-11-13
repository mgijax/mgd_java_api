package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleAnnotTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.all.domain.DenormAlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleAnnotService extends BaseService<DenormAlleleAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AlleleDAO alleleDAO;

	@Inject
	private AnnotationService annotationService;
	
	private AlleleAnnotTranslator translator = new AlleleAnnotTranslator();
	
	private SlimAlleleTranslator slimtranslator = new SlimAlleleTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
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
//		
//		log.info("AlleleAnnotService.update");
//		
//		AlleleAnnotDomain alleleAnnotDomain = new AlleleAnnotDomain();
//		List<AnnotationDomain> annotList = new ArrayList<AnnotationDomain>();
//
//		// assuming the pwi will always pass in the annotTypeKey
//		
//    	alleleAnnotDomain.setAlleleKey(domain.getAlleleKey());
//    	
//    	alleleAnnotDomain.setAllowEditTerm(domain.getAllowEditTerm());
//		
//    	// Iterate thru incoming denormalized alleleAnnot domain
//		for (int i = 0; i < domain.getAnnots().size(); i++) {
//			
//			// if processStatus == "x", then continue; no need to create domain/process anything
//			if (domain.getAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
//				continue;
//			}
//			
//			//log.info("domain index: " + i);
//			
//			DenormAnnotationDomain denormAnnotDomain = domain.getAnnots().get(i);
//		
//			// annotation (term, qualifier)
//			AnnotationDomain annotDomain = new AnnotationDomain();
//			
//			//
//			// if processStatus == "d", then process as "u"
//			// 1 annotation may have >= 1 evidence
//			// 1 evidence may be a "d", but other evidences may be "x", "u" or "c"
//			//
//			if (domain.getAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
//				annotDomain.setProcessStatus(Constants.PROCESS_UPDATE);
//			}
//			else {
//				annotDomain.setProcessStatus(denormAnnotDomain.getProcessStatus());
//			}
//					
//            annotDomain.setAnnotKey(denormAnnotDomain.getAnnotKey());
//            annotDomain.setAnnotTypeKey(denormAnnotDomain.getAnnotTypeKey());
//            annotDomain.setAnnotType(denormAnnotDomain.getAnnotType());
//            annotDomain.setObjectKey(denormAnnotDomain.getObjectKey());
//            annotDomain.setTermKey(denormAnnotDomain.getTermKey());
//            annotDomain.setTerm(denormAnnotDomain.getTerm());           
//            annotDomain.setQualifierKey(denormAnnotDomain.getQualifierKey());
//            annotDomain.setQualifierAbbreviation(denormAnnotDomain.getQualifierAbbreviation());
//            annotDomain.setQualifier(denormAnnotDomain.getQualifier());
//            annotDomain.setAllowEditTerm(domain.getAllowEditTerm());
//            
//            // evidence : create evidence list of 1 result
//            //log.info("add evidence list");
//			EvidenceDomain evidenceDomain = new EvidenceDomain();
//            List<EvidenceDomain> evidenceList = new ArrayList<EvidenceDomain>();
//            evidenceDomain.setProcessStatus(denormAnnotDomain.getProcessStatus());
//            evidenceDomain.setAnnotEvidenceKey(denormAnnotDomain.getAnnotEvidenceKey());
//            evidenceDomain.setEvidenceTermKey(denormAnnotDomain.getEvidenceTermKey());
//            evidenceDomain.setRefsKey(denormAnnotDomain.getRefsKey());
//            evidenceDomain.setCreatedByKey(denormAnnotDomain.getCreatedByKey());
//            evidenceDomain.setModifiedByKey(denormAnnotDomain.getModifiedByKey());
//            evidenceDomain.setAllNotes(denormAnnotDomain.getAllNotes());
//			
//			// add evidenceDomain to evidenceList
//			evidenceList.add(evidenceDomain);
//
//			// add evidenceList to annotDomain
//			annotDomain.setEvidence(evidenceList);
//            
//			// add annotDomain to annotList
//			annotList.add(annotDomain);         
//		}
//		
//		// add annotList to the AlleleAnnotDomain and process annotations
//		if (annotList.size() > 0) {
//			log.info("send json normalized domain to services");			
//			alleleAnnotDomain.setAnnots(annotList);
//			annotationService.process(alleleAnnotDomain.getAnnots(), user);
//		}
//		
//		log.info("repackage incoming domain as results");		
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
//		results = getResults(Integer.valueOf(domain.getAlleleKey()));
//		results.setItem(domain);
//		//log.info("results: " + results);
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
        	log.info("From the translator first annotKey: " + alleleAnnotDomain.getAnnots().get(0).getAnnotKey());
			
			List<DenormAnnotationDomain> annotList = new ArrayList<DenormAnnotationDomain>();
			
			denormAlleleAnnotDomain.setAlleleKey(alleleAnnotDomain.getAlleleKey());
			log.info("setting alleleKey: " + alleleAnnotDomain.getAlleleKey() );
	
			denormAlleleAnnotDomain.setAccid(alleleAnnotDomain.getMgiAccessionIds().get(0).getAccID());
			log.info("setting accid: " + alleleAnnotDomain.getMgiAccessionIds().get(0).getAccID());
			
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
	                    if (controllerAnnotTypeKey.equals("1021")) {
	                    	denormAnnotDomain.setTermid(annotDomain.getDoIds().get(0).getAccID());
	                    	log.info("Adding Allele DO IDs: " + annotDomain.getDoIds().get(0).getAccID());
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

						// no properties for Allele/DO annots
						
						annotList.add(denormAnnotDomain);
					}
				}
			}
			
			// sort by jnum, term		
			annotList.sort(Comparator.comparingInt(DenormAnnotationDomain::getJnum).thenComparing(DenormAnnotationDomain::getTerm));
			
			// add List of annotation domains to the denormalized allele annot domain
			denormAlleleAnnotDomain.setAnnots(annotList);
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return denormAlleleAnnotDomain;
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
		String cmd = "select count(*) as objectCount from voc_annot where _annottype_key = " + annotType;
		
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
	public List<SlimAlleleDomain> search(DenormAlleleAnnotDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();

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

		Boolean from_accession = false;
		Boolean from_annot = false;
		Boolean from_evidence = false;
		//Boolean from_property = false;
		//Boolean from_note = false;
				
		// if parameter exists, then add to where-clause
		
		if (searchDomain.getAlleleKey() != null && !searchDomain.getAlleleKey().isEmpty()) {
			where = where + "\nand v._object_key = " + searchDomain.getAlleleKey();
		}
	
		// accession id
		if (searchDomain.getAccid() != null && !searchDomain.getAccid().isEmpty()) {
			String mgiid = searchDomain.getAccid().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			where = where + "\nand lower(a.accID) = '" + mgiid.toLowerCase() + "'";
			from_accession = true;
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
				from = from + cmResults[0];
				where = where + cmResults[1];
				from_evidence = true;
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
			
			//NO NOTES FOR Allele/DO annotations?
//			if (annotDomain.getAllNotes() != null) {
//
//				value = annotDomain.getAllNotes().get(0).getNoteChunk();
//				if (value != null && !value.isEmpty()) {
//
//					if (annotDomain.getAllNotes().get(0).getNoteTypeKey() != null 
//							&& !annotDomain.getAllNotes().get(0).getNoteTypeKey().isEmpty()) {
//						where = where + "\nand n._notetype_key = " + annotDomain.getAllNotes().get(0).getNoteTypeKey();
//					}
//
//					where = where + "\nand n.note ilike '" + value + "'";
//					from_note = true;
//				}
//
//			}
		}
		
		// dependencies
//		if (from_note == true) {
//			from_evidence = true;
//		}
		if (from_evidence == true) {
			from_annot = true;
		}
		
		// from/where construction
		if (from_accession == true) {
			from = from + ", gxd_genotype_acc_view a";
			where = where + "\nand v._object_key = a._object_key" 
					+ "\nand a._mgitype_key = " + mgiTypeKey;
		}
		if (from_annot == true) {
			from = from + ", voc_annot va";
			where = where + "\nand v._object_key = va._object_key" 
					+ "\nand v._logicaldb_key = 1"
					+ "\nand v.preferred = 1"
					+ "\nand va._annottype_key = " + searchDomain.getAnnots().get(0).getAnnotTypeKey();
		}
		if (from_evidence == true) {
			from = from + ", voc_evidence e";
			where = where + "\nand va._annot_key = e._annot_key";
		}
//		if (from_property == true) {
//			from = from + ", voc_evidence_property p";
//			where = where + "\nand e._annotevidence_key = p._annotevidence_key";
//		}
//		if (from_note == true) {
//			from = from + ", mgi_note_vocevidence_view n";
//			where = where + "\nand e._annotevidence_key = n._object_key";
//		}

		// removed "limit"
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info("searchCmd: " + cmd);

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
	
					SlimAlleleDomain domain = new SlimAlleleDomain();
					domain = slimtranslator.translate(alleleDAO.get(prevObjectKey));				
					alleleDAO.clear();				
					results.add(domain);
					
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
				
				// if last record, then add to result set
				if (rs.isLast() == true) {
					
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					prevDescription = prevStrain + " " + prevDescription;
					
					SlimAlleleDomain domain = new SlimAlleleDomain();
					domain = slimtranslator.translate(alleleDAO.get(prevObjectKey));				
					alleleDAO.clear();				
					results.add(domain);
				}
								
			}
			sqlExecutor.cleanup();
			
			// now we order by description - see note above at first 'select = '
			//results.sort(Comparator.comparing(SlimAlleleDomain::getGenotypeDisplay, String.CASE_INSENSITIVE_ORDER));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@Transactional	
	public List<MGIReferenceAssocDomain> validateAlleleReference(SlimAlleleRefAssocDomain searchDomain) {
		// return a list of Allele/Reference associations that do not exist for this Allele/Reference
		// returns empty list of values if validation fails

		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();
		
//		String cmd = "\nselect distinct g._allele_key"
//				+ "\nfrom GXD_AlleleGenotype g, ALL_Allele a" 
//				+ "\nwhere g._Allele_key = a._Allele_key"
//				+ "\nand a.isWildType = 0"
//				+ "\nand g._Genotype_key = " + searchDomain.getGenotypeKey()
//				+ "\nand not exists (select 1 from MGI_Reference_Assoc a where a._MGIType_key = 11" 
//					+ "\nand a._Object_key = g._Allele_key"
//					+ "\nand a._Refs_key = " + searchDomain.getRefsKey() + ")";
//		
//		log.info(cmd);
//		
//		try {
//			ResultSet rs = sqlExecutor.executeProto(cmd);
//			while (rs.next()) {
//				MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
//				domain.setProcessStatus(Constants.PROCESS_CREATE);
//				domain.setObjectKey(rs.getString("_allele_key"));
//				domain.setMgiTypeKey("11");
//				domain.setRefAssocType("Used-FC");
//				domain.setRefsKey(searchDomain.getRefsKey());
//				results.add(domain);
//			}
//			sqlExecutor.cleanup();			
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return results;
	}	
		
}
