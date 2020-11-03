package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleMutationDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocViewDomain;
import org.jax.mgi.mgd.api.model.img.translator.ImagePaneAssocViewTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipAlleleDriverGeneDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipAlleleDriverGeneTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerNoteTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class AlleleTranslator extends BaseEntityDomainTranslator<Allele, AlleleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AlleleDomain entityToDomain(Allele entity) {
		
		AlleleDomain domain = new AlleleDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setIsWildType(String.valueOf(entity.getIsWildType()));
		domain.setIsExtinct(String.valueOf(entity.getIsExtinct()));
		domain.setIsMixed(String.valueOf(entity.getIsMixed()));

		domain.setInheritanceModeKey(String.valueOf(entity.getInheritanceMode().get_term_key()));
		domain.setInheritanceMode(entity.getInheritanceMode().getTerm());
		domain.setAlleleTypeKey(String.valueOf(entity.getAlleleType().get_term_key()));
		domain.setAlleleType(entity.getAlleleType().getTerm());
		domain.setAlleleStatusKey(String.valueOf(entity.getAlleleStatus().get_term_key()));
		domain.setAlleleStatus(entity.getAlleleStatus().getTerm());	
		domain.setMarkerAlleleStatusKey(String.valueOf(entity.getMarkerAlleleStatus().get_term_key()));
		domain.setMarkerAlleleStatus(entity.getMarkerAlleleStatus().getTerm());
		domain.setTransmissionKey(String.valueOf(entity.getTransmission().get_term_key()));
		domain.setTransmission(entity.getTransmission().getTerm());
		domain.setCollectionKey(String.valueOf(entity.getCollection().get_term_key()));
		domain.setCollection(entity.getCollection().getTerm());
		domain.setStrainOfOriginKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setStrainOfOrigin(entity.getStrain().getStrain());
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getApprovedBy() != null) {
			domain.setApprovedByKey(entity.getApprovedBy().get_user_key().toString());
			domain.setApprovedBy(entity.getApprovedBy().getLogin());
			domain.setApproval_date(dateFormatNoTime.format(entity.getApproval_date()));
		}

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		// other accession ids
		if (entity.getOtherAccessionIds() != null && !entity.getOtherAccessionIds().isEmpty()) {
			AccessionTranslator accessionTranslator = new AccessionTranslator();	
			Iterable<AccessionDomain> t = accessionTranslator.translateEntities(entity.getOtherAccessionIds());
			domain.setOtherAccIDs(IteratorUtils.toList(t.iterator()));
			domain.getOtherAccIDs().sort(Comparator.comparing(AccessionDomain::getLogicaldb));
		}
		
		// marker stuff
		if (entity.getMarker() != null && !entity.getMarker().getSymbol().isEmpty()) {
			domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
			domain.setMarkerSymbol(entity.getMarker().getSymbol());			
			domain.setChromosome(entity.getMarker().getChromosome());
			domain.setMarkerStatusKey(String.valueOf(entity.getMarker().getMarkerStatus().get_marker_status_key()));
			domain.setMarkerStatus(entity.getMarker().getMarkerStatus().getStatus());
			
			// reference can be null
			if (entity.getMarkerReference() != null) {
				domain.setRefsKey(String.valueOf(entity.getMarkerReference().get_refs_key()));
				domain.setJnumid(entity.getMarkerReference().getReferenceCitationCache().getJnumid());
				domain.setJnum(entity.getMarkerReference().getReferenceCitationCache().getNumericPart());
				domain.setShort_citation(entity.getMarkerReference().getReferenceCitationCache().getShort_citation());
			}
			
			// marker detail clip
			if (entity.getMarker().getDetailClipNote() != null && !entity.getMarker().getDetailClipNote().isEmpty()) {
				MarkerNoteTranslator noteTranslator = new MarkerNoteTranslator();
				Iterable<MarkerNoteDomain> note = noteTranslator.translateEntities(entity.getMarker().getDetailClipNote());
				domain.setDetailClip(note.iterator().next());
			}
		}
		
		// reference associations
		if (entity.getRefAssocs() != null && !entity.getRefAssocs().isEmpty()) {
			MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
			Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
			domain.getRefAssocs().sort(Comparator.comparingInt(MGIReferenceAssocDomain::getAllowOnlyOne).reversed().thenComparing(MGIReferenceAssocDomain::getRefAssocTypeKey));

			// save molecular, mixed references
			for (int r = 0; r < domain.getRefAssocs().size(); r++) {
				if (domain.getRefAssocs().get(r).getRefAssocTypeKey().equals("1012")) {
					domain.setMolRefKey(domain.getRefAssocs().get(r).getRefsKey());
				}
				if (domain.getRefAssocs().get(r).getRefAssocTypeKey().equals("1024")) {
					domain.setMixedRefKey(domain.getRefAssocs().get(r).getRefsKey());
				}				
			}
		}	

		// synonyms
		if (entity.getSynonyms() != null && !entity.getSynonyms().isEmpty()) {
			MGISynonymTranslator synonymTranslator = new MGISynonymTranslator();
			Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getSynonyms());
			domain.setSynonyms(IteratorUtils.toList(i.iterator()));
			domain.getSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
		}
		
		// molecular mutations
		if (entity.getMutations() != null && !entity.getMutations().isEmpty()) {
			AlleleMutationTranslator mutationTranslator = new AlleleMutationTranslator();
			Iterable<AlleleMutationDomain> i = mutationTranslator.translateEntities(entity.getMutations());
			domain.setMutations(IteratorUtils.toList(i.iterator()));			
			domain.getMutations().sort(Comparator.comparing(AlleleMutationDomain::getMutation));
		}
		
		// subtype annotations
		if (entity.getSubtypeAnnots() != null && !entity.getSubtypeAnnots().isEmpty()) {
			AnnotationTranslator annotTranslator = new AnnotationTranslator();
			Iterable<AnnotationDomain> i = annotTranslator.translateEntities(entity.getSubtypeAnnots());
			domain.setSubtypeAnnots(IteratorUtils.toList(i.iterator()));
		}

		// mutant cell lines, parent cell line (at most 1)
		if (entity.getMutantCellLines() != null && !entity.getMutantCellLines().isEmpty()) {
			AlleleCellLineTranslator allelecellLineTranslator = new AlleleCellLineTranslator();
			Iterable<AlleleCellLineDomain> i = allelecellLineTranslator.translateEntities(entity.getMutantCellLines());
			domain.setMutantCellLineAssocs(IteratorUtils.toList(i.iterator()));
		}
		
		// driver genes
		if (entity.getDriverGenes() != null && !entity.getDriverGenes().isEmpty()) {
			RelationshipAlleleDriverGeneTranslator driverTranslator = new RelationshipAlleleDriverGeneTranslator();
			Iterable<RelationshipAlleleDriverGeneDomain> i = driverTranslator.translateEntities(entity.getDriverGenes());
			domain.setDriverGenes(IteratorUtils.toList(i.iterator()));
		}

		// imagepane associations by allele
		if (entity.getImagePaneAssocs() != null && !entity.getImagePaneAssocs().isEmpty()) {
			ImagePaneAssocViewTranslator imagePaneTranslator = new ImagePaneAssocViewTranslator();
			Iterable<ImagePaneAssocViewDomain> t = imagePaneTranslator.translateEntities(entity.getImagePaneAssocs());
			domain.setImagePaneAssocs(IteratorUtils.toList(t.iterator()));
			domain.getImagePaneAssocs().sort(Comparator.comparing(ImagePaneAssocViewDomain::getIsPrimary).reversed());
		}
		
		// at most one note
		if (entity.getGeneralNote() != null && !entity.getGeneralNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getGeneralNote());
			domain.setGeneralNote(note.iterator().next());
		}
		
		// at most one note
		if (entity.getMolecularNote() != null && !entity.getMolecularNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getMolecularNote());
			domain.setMolecularNote(note.iterator().next());
		}

		// at most one note
		if (entity.getNomenNote() != null && !entity.getNomenNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getNomenNote());
			domain.setNomenNote(note.iterator().next());
		}
		
		// at most one note
		if (entity.getInducibleNote() != null && !entity.getInducibleNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getInducibleNote());
			domain.setInducibleNote(note.iterator().next());
		}

		// at most one note
		if (entity.getProidNote() != null && !entity.getProidNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getProidNote());
			domain.setProidNote(note.iterator().next());
		}

		// at most one note
		if (entity.getCreNote() != null && !entity.getCreNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getCreNote());
			domain.setCreNote(note.iterator().next());
		}
		
		// at most one note
		if (entity.getIkmcNote() != null && !entity.getIkmcNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getIkmcNote());
			domain.setIkmcNote(note.iterator().next());
		}
		
		// do annotations
		// not needed per lec/11/03/2020
		// causes hiberate/collection issue
//		if (entity.getDoAnnots() != null && !entity.getDoAnnots().isEmpty()) {
//			AnnotationTranslator annotTranslator = new AnnotationTranslator();
//			Iterable<AnnotationDomain> i = annotTranslator.translateEntities(entity.getDoAnnots());
//			domain.setDoAnnots(IteratorUtils.toList(i.iterator()));
//		}
		
		return domain;
	}
	
}
