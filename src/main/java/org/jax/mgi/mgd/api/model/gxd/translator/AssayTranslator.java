package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayNoteDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelRowDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jboss.logging.Logger;

public class AssayTranslator extends BaseEntityDomainTranslator<Assay, AssayDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AssayDomain entityToDomain(Assay entity) {

		AssayDomain domain = new AssayDomain();

		domain.setAssayKey(String.valueOf(entity.get_assay_key()));

//      1 InSitu | RNA in situ                    
//      2 North  | Northern blot              
//      3 S1Nuc  | Nuclease S1                 
//      4 RNAse  | RNase protection      
//      5 RTPCR  | RT-PCR                           
//      6 Immuno | Immunohistochemistry          
//      8 West   | Western blot               
//      9 Knock  | In situ reporter (knock in)       
//     10 Transg | In situ reporter (transgenic)                 
//     11 Recomb | Recombinase reporter     
		
		if (entity.getAssayType().get_assaytype_key() == 1) {
			domain.setAssayTypeAbbrev("InSitu");
		}
		else if (entity.getAssayType().get_assaytype_key() == 2) {
			domain.setAssayTypeAbbrev("North");
		}
		else if (entity.getAssayType().get_assaytype_key() == 3) {
			domain.setAssayTypeAbbrev("S1Nuc");
		}
		else if (entity.getAssayType().get_assaytype_key() == 4) {
			domain.setAssayTypeAbbrev("RNAse");
		}
		else if (entity.getAssayType().get_assaytype_key() == 5) {
			domain.setAssayTypeAbbrev("RTPCR");
		}
		else if (entity.getAssayType().get_assaytype_key() == 6) {
			domain.setAssayTypeAbbrev("Immuno");
		}
		else if (entity.getAssayType().get_assaytype_key() == 8) {
			domain.setAssayTypeAbbrev("West");
		}
		else if (entity.getAssayType().get_assaytype_key() == 9) {
			domain.setAssayTypeAbbrev("Knock");
		}
		else if (entity.getAssayType().get_assaytype_key() == 10) {
			domain.setAssayTypeAbbrev("Transg");
		}
		else if (entity.getAssayType().get_assaytype_key() == 11) {
			domain.setAssayTypeAbbrev("Recomb");
		}
		
		domain.setAssayDisplay(entity.getReference().getReferenceCitationCache().getJnumid() + "; " + domain.getAssayTypeAbbrev() + "; " + entity.getMarker().getSymbol());	

		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());	
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		// image pane
		if (entity.getImagePane() != null) {
			domain.setImagePaneKey(String.valueOf(entity.getImagePane().get_image_key()));
		}
		
		// reporter gene
		if (entity.getReporterGene() != null) {
			domain.setReporterGeneKey(String.valueOf(entity.getReporterGene().get_term_key()));
			domain.setReporterGeneTerm(entity.getReporterGene().getTerm());
		}

		// neither prep
		if (entity.getAntibodyPrep() == null && entity.getProbePrep() == null) {
			domain.setDetectionKey("3");
		}		
		// antibody prep
		else if (entity.getAntibodyPrep() != null) {
			AntibodyPrepTranslator i = new AntibodyPrepTranslator();
			domain.setAntibodyPrep(i.translate(entity.getAntibodyPrep()));
			domain.setDetectionKey("2");
		}
		// probe prep
		else if (entity.getProbePrep() != null) {
			ProbePrepTranslator i = new ProbePrepTranslator();
			domain.setProbePrep(i.translate(entity.getProbePrep()));
			domain.setDetectionKey("1");			
		}
			
		// assay note
		if (entity.getAssayNote() != null && !entity.getAssayNote().isEmpty()) {
			AssayNoteTranslator assayNoteTranslator = new AssayNoteTranslator();
			Iterable<AssayNoteDomain> i = assayNoteTranslator.translateEntities(entity.getAssayNote());
			domain.setAssayNote(i.iterator().next());
		}

		// specimens
		if (entity.getSpecimens() != null && !entity.getSpecimens().isEmpty()) {
			SpecimenTranslator specimenTranslator = new SpecimenTranslator();
			Iterable<SpecimenDomain> i = specimenTranslator.translateEntities(entity.getSpecimens());
			domain.setSpecimens(IteratorUtils.toList(i.iterator()));
			domain.getSpecimens().sort(Comparator.comparingInt(SpecimenDomain::getSequenceNum));
		}

		// gel lanes
		if (entity.getGelLanes() != null && !entity.getGelLanes().isEmpty()) {
			GelLaneTranslator gellaneTranslator = new GelLaneTranslator();
			Iterable<GelLaneDomain> i = gellaneTranslator.translateEntities(entity.getGelLanes());
			domain.setGelLanes(IteratorUtils.toList(i.iterator()));
			domain.getGelLanes().sort(Comparator.comparingInt(GelLaneDomain::getSequenceNum));
		}

		// gel rows
		if (entity.getGelRows() != null && !entity.getGelRows().isEmpty()) {
			GelRowTranslator gelrowTranslator = new GelRowTranslator();
			Iterable<GelRowDomain> i = gelrowTranslator.translateEntities(entity.getGelRows());
			domain.setGelRows(IteratorUtils.toList(i.iterator()));
			domain.getGelRows().sort(Comparator.comparingInt(GelRowDomain::getSequenceNum));
		}

		
		return domain;
	}

}
