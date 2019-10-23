package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.DenormGenotypeMPDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeMPDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeAlleleReferenceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeMPTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGenotypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.AnnotationHeaderDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationHeaderDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationMPDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GenotypeMPService extends BaseService<DenormGenotypeMPDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GenotypeDAO genotypeDAO;

	@Inject
	private AnnotationService annotationService;
	@Inject
	private AnnotationHeaderDAO annotationHeaderDAO;
	
	private GenotypeMPTranslator translator = new GenotypeMPTranslator();
	private SlimGenotypeTranslator slimtranslator = new SlimGenotypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeKey = "12";

	@Transactional
	public SearchResults<DenormGenotypeMPDomain> create(DenormGenotypeMPDomain domain, User user) {	
		log.info("GenotypeMPService.create");
		SearchResults<DenormGenotypeMPDomain> results = new SearchResults<DenormGenotypeMPDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public SearchResults<DenormGenotypeMPDomain> update(DenormGenotypeMPDomain domain, User user) {
		// translate pwi/incoming denormlized json domain to list of normalied domain (GenotypeMPDomain)
		// use normalized domain to process hibernate entities
		
		log.info("GenotypeMPService.update");
		
		GenotypeMPDomain mpDomain = new GenotypeMPDomain();
		List<AnnotationDomain> annotList = new ArrayList<AnnotationDomain>();

    	mpDomain.setGenotypeKey(domain.getGenotypeKey());
		//mpDomain.setGenotypeDisplay(domain.getGenotypeDisplay());
    	mpDomain.setMpHeaders(domain.getMpHeaders());
    	mpDomain.setAllowEditTerm(domain.getAllowEditTerm());
		
    	// incoming denormalized MP json domain
		for (int i = 0; i < domain.getMpAnnots().size(); i++) {
			
			// if processStatus == "x", then continue; no need to create domain/process anything
			if (domain.getMpAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_NOTDIRTY)) {
				continue;
			}
			
			//log.info("domain index: " + i);
			
			AnnotationMPDomain annotMPDomain = domain.getMpAnnots().get(i);
		
			// annotation (term, qualifier)
			AnnotationDomain annotDomain = new AnnotationDomain();
			
			//
			// if processStatus == "d", then process as "u"
			// 1 annotation may have >= 1 evidence
			// 1 evidence may be a "d", bu the other evidence may be "x", "u" or "c"
			//
			if (domain.getMpAnnots().get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				annotDomain.setProcessStatus(Constants.PROCESS_UPDATE);
			}
			else {
				annotDomain.setProcessStatus(annotMPDomain.getProcessStatus());
			}
					
            annotDomain.setAnnotKey(annotMPDomain.getAnnotKey());
            annotDomain.setAnnotTypeKey(annotMPDomain.getAnnotTypeKey());
            annotDomain.setAnnotType(annotMPDomain.getAnnotType());
            annotDomain.setObjectKey(annotMPDomain.getObjectKey());
            annotDomain.setTermKey(annotMPDomain.getTermKey());
            annotDomain.setTerm(annotMPDomain.getTerm());           
            annotDomain.setQualifierKey(annotMPDomain.getQualifierKey());
            annotDomain.setQualifierAbbreviation(annotMPDomain.getQualifierAbbreviation());
            annotDomain.setQualifier(annotMPDomain.getQualifier());
            annotDomain.setAllowEditTerm(domain.getAllowEditTerm());
            
            // evidence : create evidence list of 1 result
            //log.info("add evidence list");
			EvidenceDomain evidenceDomain = new EvidenceDomain();
            List<EvidenceDomain> evidenceList = new ArrayList<EvidenceDomain>();
            evidenceDomain.setProcessStatus(annotMPDomain.getProcessStatus());
            evidenceDomain.setAnnotEvidenceKey(annotMPDomain.getAnnotEvidenceKey());
            evidenceDomain.setEvidenceTermKey(annotMPDomain.getEvidenceTermKey());
            evidenceDomain.setRefsKey(annotMPDomain.getRefsKey());
            evidenceDomain.setCreatedByKey(annotMPDomain.getCreatedByKey());
            evidenceDomain.setModifiedByKey(annotMPDomain.getModifiedByKey());
            evidenceDomain.setAllNotes(annotMPDomain.getAllNotes());
			
			// sex-specificity : create evidence-property list of 1 result
            //log.info("add evidence-property");
			EvidencePropertyDomain evidencePropertyDomain = new EvidencePropertyDomain();			
			List<EvidencePropertyDomain > evidencePropertyList = new ArrayList<EvidencePropertyDomain>();
			evidencePropertyDomain.setProcessStatus(annotMPDomain.getProcessStatus());
			evidencePropertyDomain.setEvidencePropertyKey(annotMPDomain.getEvidencePropertyKey());
			evidencePropertyDomain.setPropertyTermKey(annotMPDomain.getPropertyTermKey());
			evidencePropertyDomain.setValue(annotMPDomain.getMpSexSpecificityValue());
			evidencePropertyList.add(evidencePropertyDomain);
			
			// add sex-specificity to the evidenceDomain
			evidenceDomain.setMpSexSpecificity(evidencePropertyList);

			// add evidenceDomain to evidenceList
			evidenceList.add(evidenceDomain);

			// add evidenceList to annotDomain
			annotDomain.setEvidence(evidenceList);

			// add annotDomain to annotList
			annotList.add(annotDomain);         
		}
		
		// add annotList to the mpDomain and process annotations
		if (annotList.size() > 0) {
			log.info("send json normalized domain to services");			
			mpDomain.setMpAnnots(annotList);
			annotationService.process(mpDomain.getMpAnnots(), user);
			
			// process change to annotationHeaderDomian.getSequenceNum()
			List<AnnotationHeaderDomain> headerList = mpDomain.getMpHeaders();
			if (headerList != null) {
				for (int j = 0; j < headerList.size(); j++) {
					AnnotationHeaderDomain annotationHeaderDomain = headerList.get(j);
					AnnotationHeader annotationHeaderEntity = annotationHeaderDAO.get(Integer.valueOf(annotationHeaderDomain.getAnnotHeaderKey()));
		
					if (annotationHeaderDomain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
						if (!annotationHeaderEntity.getSequenceNum().equals(annotationHeaderDomain.getSequenceNum())) {	
							annotationHeaderEntity.setSequenceNum(annotationHeaderDomain.getSequenceNum());
							annotationHeaderEntity.setModification_date(new Date());
							annotationHeaderEntity.setModifiedBy(user);
							annotationHeaderEntity.setApproval_date(new Date());
							annotationHeaderEntity.setApprovedBy(user);						
							annotationHeaderDAO.update(annotationHeaderEntity);
						}
					}	
				}
			}
		}
		
		log.info("repackage incoming domain as results");		
		SearchResults<DenormGenotypeMPDomain> results = new SearchResults<DenormGenotypeMPDomain>();
		results.setItem(domain);
		log.info("results: " + results);
		return results;
	}

	@Transactional
	public SearchResults<DenormGenotypeMPDomain> delete(Integer key, User user) {
		log.info("GenotypeMPService.delete");
		SearchResults<DenormGenotypeMPDomain> results = new SearchResults<DenormGenotypeMPDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public DenormGenotypeMPDomain get(Integer key) {
    	// get the DAO/entity and translate -> domain -> MP denormalized domain

		DenormGenotypeMPDomain mpDomain = new DenormGenotypeMPDomain();

    	try {
        	GenotypeMPDomain domain = new GenotypeMPDomain();   		
			domain = translator.translate(genotypeDAO.get(key));
			List<AnnotationMPDomain> annotList = new ArrayList<AnnotationMPDomain>();
			
			mpDomain.setGenotypeKey(domain.getGenotypeKey());
			mpDomain.setGenotypeDisplay(domain.getGenotypeDisplay());
			mpDomain.setAccid(domain.getMgiAccessionIds().get(0).getAccID());
			mpDomain.setMpHeaders(domain.getMpHeaders());
			
			if (domain.getMpAnnots() != null) {
				
				for (int i = 0; i < domain.getMpAnnots().size(); i++) {
	
					//log.info("domain.getMpAnnots.row: " + i);
					
					// annotation (term, qualifier)
					AnnotationDomain annotDomain = domain.getMpAnnots().get(i);
	
					// evidence
					for (int j = 0; j < domain.getMpAnnots().get(i).getEvidence().size(); j++) {
	
						//log.info("domain.getMpAnnots.getEvidence.row: " + j);

						// annotation (term, qualifier)
						AnnotationMPDomain annotMPDomain = new AnnotationMPDomain();
						annotMPDomain.setProcessStatus(annotDomain.getProcessStatus());
	                    annotMPDomain.setAnnotKey(annotDomain.getAnnotKey());
	                    annotMPDomain.setAnnotTypeKey(annotDomain.getAnnotTypeKey());
	                    annotMPDomain.setAnnotType(annotDomain.getAnnotType());
	                    annotMPDomain.setObjectKey(annotDomain.getObjectKey());
	                    annotMPDomain.setTermKey(annotDomain.getTermKey());
	                    annotMPDomain.setTerm(annotDomain.getTerm());
	                    annotMPDomain.setQualifierKey(annotDomain.getQualifierKey());
		                annotMPDomain.setQualifierAbbreviation(annotDomain.getQualifierAbbreviation());
		                annotMPDomain.setQualifier(annotDomain.getQualifier());
		                annotMPDomain.setMpid(annotDomain.getMpIds().get(0).getAccID());
		
		                // evidence
						EvidenceDomain evidenceDomain = annotDomain.getEvidence().get(j);
		                annotMPDomain.setAnnotEvidenceKey(evidenceDomain.getAnnotEvidenceKey());
		                annotMPDomain.setEvidenceTermKey(evidenceDomain.getEvidenceTermKey());
		                annotMPDomain.setEvidenceTerm(evidenceDomain.getEvidenceTerm());
		                annotMPDomain.setEvidenceAbbreviation(evidenceDomain.getEvidenceAbbreviation());
		                annotMPDomain.setRefsKey(evidenceDomain.getRefsKey());
		                annotMPDomain.setJnumid(evidenceDomain.getJnumid());
		                annotMPDomain.setJnum(Integer.valueOf(evidenceDomain.getJnum()));
		                annotMPDomain.setShort_citation(evidenceDomain.getShort_citation());
		                annotMPDomain.setCreatedByKey(evidenceDomain.getCreatedByKey());
		                annotMPDomain.setCreatedBy(evidenceDomain.getCreatedBy());
		                annotMPDomain.setModifiedByKey(evidenceDomain.getModifiedByKey());
		                annotMPDomain.setModifiedBy(evidenceDomain.getModifiedBy());
						annotMPDomain.setCreation_date(evidenceDomain.getCreation_date());
						annotMPDomain.setModification_date(evidenceDomain.getModification_date());
						annotMPDomain.setAllNotes(evidenceDomain.getAllNotes());

						// evidence-property : sex-specificity
						EvidencePropertyDomain evidencePropertyDomain = evidenceDomain.getMpSexSpecificity().get(0);
						annotMPDomain.setEvidencePropertyKey(evidencePropertyDomain.getEvidencePropertyKey());
						annotMPDomain.setPropertyTermKey(evidencePropertyDomain.getPropertyTermKey());
						annotMPDomain.setMpSexSpecificityValue(evidencePropertyDomain.getValue());
						
						annotList.add(annotMPDomain);
					}
				}
			}

			// add all annotResults to the mpDomain
			mpDomain.setMpAnnots(annotList);
			
			// sort by jnum, term		
			annotList.sort(Comparator.comparingInt(AnnotationMPDomain::getJnum).thenComparing(AnnotationMPDomain::getTerm));				
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return mpDomain;
	}

	@Transactional
	public SearchResults<DenormGenotypeMPDomain> getResults(Integer key) {
		// get the denormalized domain -> results
		SearchResults<DenormGenotypeMPDomain> results = new SearchResults<DenormGenotypeMPDomain>();
		results.setItem(get(key));
		return results;
	}
		
	@Transactional	
	public SearchResults<DenormGenotypeMPDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<DenormGenotypeMPDomain> results = new SearchResults<DenormGenotypeMPDomain>();
		String cmd = "select count(*) as objectCount from voc_annot where _annottype_key = 1002";
		
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
	public List<SlimGenotypeDomain> search(DenormGenotypeMPDomain searchDomain) {
		// using searchDomain fields, generate SQL command
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
		String limit = Constants.SEARCH_RETURN_LIMIT;
		
		String value;

		Boolean from_accession = false;
		Boolean from_annot = false;
		Boolean from_evidence = false;
		Boolean from_property = false;
		Boolean from_note = false;
				
		// if parameter exists, then add to where-clause
		
		if (searchDomain.getGenotypeKey() != null && !searchDomain.getGenotypeKey().isEmpty()) {
			where = where + "\nand v._object_key = " + searchDomain.getGenotypeKey();
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
		
		if (searchDomain.getGenotypeDisplay() != null && !searchDomain.getGenotypeDisplay().isEmpty()) {
			where = where + "\nand v.description ilike '" + searchDomain.getGenotypeDisplay() + "'";		
		}
		
		if (searchDomain.getMpAnnots() != null) {
						
			AnnotationMPDomain annotDomain = searchDomain.getMpAnnots().get(0);
		
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

			if (annotDomain.getCreatedBy() != null
					|| annotDomain.getModifiedBy() != null
					|| annotDomain.getCreation_date() != null
					|| annotDomain.getModification_date() != null) {			
				if (!annotDomain.getCreatedBy().isEmpty()
						|| !annotDomain.getModifiedBy().isEmpty()
						|| !annotDomain.getCreation_date().isEmpty()
						|| !annotDomain.getModification_date().isEmpty()) {
	
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
				}
			}
			
			value = annotDomain.getMpSexSpecificityValue();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand p._propertyterm_key = " + annotDomain.getPropertyTermKey();
				where = where + "\nand p.value ilike '" + value + "'";
				from_evidence = true;
				from_property = true;
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
				
				value = annotDomain.getAllNotes().get(0).getNoteTypeKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand n._notetype_key = " + value;
					from_note = true;
				}
				
				value = annotDomain.getAllNotes().get(0).getNoteChunk();
				if (value != null && !value.isEmpty()) {
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
					+ "\nand va._annottype_key = 1002";
		}
		if (from_evidence == true) {
			from = from + ", voc_evidence_view e";
			where = where + "\nand va._annot_key = e._annot_key";
		}
		if (from_property == true) {
			from = from + ", voc_evidence_property p";
			where = where + "\nand e._annotevidence_key = p._annotevidence_key";
		}
		if (from_note == true) {
			from = from + ", mgi_note_vocevidence_view n";
			where = where + "\nand e._annotevidence_key = n._object_key";
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
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
				
				// if last record, then add to result set
				if (rs.isLast() == true) {
					addResults = true;
				}
				
				if (addResults) {

					prevDescription = prevStrain + " " + prevDescription;
	
					SlimGenotypeDomain domain = new SlimGenotypeDomain();
					domain = slimtranslator.translate(genotypeDAO.get(prevObjectKey));				
					domain.setGenotypeDisplay(prevDescription);
					genotypeDAO.clear();				
					results.add(domain);
					
					prevObjectKey = newObjectKey;
					prevStrain = newStrain;
					prevDescription = newDescription;
					addResults = false;
				}
			}
			sqlExecutor.cleanup();
			
			// now we order by description - see note above at first 'select = '
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
