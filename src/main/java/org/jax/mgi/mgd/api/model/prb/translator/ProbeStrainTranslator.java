package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainGenotypeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class ProbeStrainTranslator extends BaseEntityDomainTranslator<ProbeStrain, ProbeStrainDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private NoteTranslator noteTranslator = new NoteTranslator();
	private AnnotationTranslator annotationTranslator = new AnnotationTranslator();
	private ProbeStrainMarkerTranslator markerTranslator = new ProbeStrainMarkerTranslator();
	private ProbeStrainGenotypeTranslator genotypeTranslator = new ProbeStrainGenotypeTranslator();
	private MGISynonymTranslator synonymTranslator = new MGISynonymTranslator();
	private MGIReferenceAssocTranslator refTranslator = new MGIReferenceAssocTranslator();
	
	@Override
	protected ProbeStrainDomain entityToDomain(ProbeStrain entity) {
		
		ProbeStrainDomain domain = new ProbeStrainDomain();
		
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setStrain(entity.getStrain());
		domain.setStandard(String.valueOf(entity.getStandard()));
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));
		domain.setGeneticBackground(String.valueOf(entity.getGeneticBackground()));
		domain.setSpeciesKey(String.valueOf(entity.getSpecies().get_term_key()));
		domain.setSpecies(entity.getSpecies().getTerm());
		domain.setStrainTypeKey(String.valueOf(entity.getStrainType().get_term_key()));
		domain.setStrainType(entity.getStrainType().getTerm());
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

		// other accession ids
		if (entity.getOtherAccessionIds() != null && !entity.getOtherAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> t = accessionTranslator.translateEntities(entity.getOtherAccessionIds());
			domain.setOtherAccIds(IteratorUtils.toList(t.iterator()));
			domain.getOtherAccIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb));
		}
		
		// at most one strainOriginNote
		if (entity.getStrainOriginNote() != null && !entity.getStrainOriginNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getStrainOriginNote());
			domain.setStrainOriginNote(note.iterator().next());
		}

		// at most one impcColonyNote
		if (entity.getImpcColonyNote() != null && !entity.getImpcColonyNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getImpcColonyNote());
			domain.setImpcColonyNote(note.iterator().next());
		}

		// at most one nomenclatureNote
		if (entity.getNomenclatureNote() != null && !entity.getNomenclatureNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getNomenclatureNote());
			domain.setNomenclatureNote(note.iterator().next());
		}

		// at most one mutantCellLineNote
		if (entity.getMutantCellLineNote() != null && !entity.getMutantCellLineNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getMutantCellLineNote());
			domain.setMutantCellLineNote(note.iterator().next());
		}

		// strain attributes
		if (entity.getAttributes() != null && !entity.getAttributes().isEmpty()) {
			Iterable<AnnotationDomain> t = annotationTranslator.translateEntities(entity.getAttributes());
			domain.setAttributes(IteratorUtils.toList(t.iterator()));
			domain.getAttributes().sort(Comparator.comparing(AnnotationDomain::getTerm));
		}

		// needs review
		if (entity.getNeedsReview() != null && !entity.getNeedsReview().isEmpty()) {
			Iterable<AnnotationDomain> t = annotationTranslator.translateEntities(entity.getNeedsReview());
			domain.setNeedsReview(IteratorUtils.toList(t.iterator()));
			domain.getNeedsReview().sort(Comparator.comparing(AnnotationDomain::getTerm));
		}
		
		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			Iterable<ProbeStrainMarkerDomain> t = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(t.iterator()));
			domain.getMarkers().sort(Comparator.comparing(ProbeStrainMarkerDomain::getQualifierTerm));
		}

		// genotypes
		if (entity.getGenotypes() != null && !entity.getGenotypes().isEmpty()) {
			Iterable<ProbeStrainGenotypeDomain> t = genotypeTranslator.translateEntities(entity.getGenotypes());
			domain.setGenotypes(IteratorUtils.toList(t.iterator()));
			domain.getGenotypes().sort(Comparator.comparing(ProbeStrainGenotypeDomain::getQualifierTerm));
		}
	
		// synonyms
		if (entity.getSynonyms() != null && !entity.getSynonyms().isEmpty()) {
			Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getSynonyms());
			domain.setSynonyms(IteratorUtils.toList(i.iterator()));
			domain.getSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
		}

//		// references
//		if (entity.getRefAssocs() != null && !entity.getRefAssocs().isEmpty()) {
//			Iterable<MGIReferenceAssocDomain> i = refTranslator.translateEntities(entity.getRefAssocs());
//			domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
//			domain.getRefAssocs().sort(Comparator.comparing(MGIReferenceAssocDomain::getRefAssocType).thenComparingInt(MGIReferenceAssocDomain::getJnum));
//		}
		
		return domain;
	}

}
