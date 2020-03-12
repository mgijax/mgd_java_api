package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleMutationDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleTranslator extends BaseEntityDomainTranslator<Allele, AlleleDomain> {

	private AccessionTranslator accessionTranslator = new AccessionTranslator();	
	private MGISynonymTranslator synonymTranslator = new MGISynonymTranslator();
	
	@Override
	protected AlleleDomain entityToDomain(Allele entity) {
		
		AlleleDomain domain = new AlleleDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setIsWildType(entity.getIsWildType());
		domain.setIsExtinct(entity.getIsExtinct());
		domain.setIsMixed(entity.getIsMixed());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// primary mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		// other accession ids
		if (entity.getOtherAccessionIds() != null && !entity.getOtherAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> t = accessionTranslator.translateEntities(entity.getOtherAccessionIds());
			domain.setOtherAccIds(IteratorUtils.toList(t.iterator()));
			domain.getOtherAccIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb));
		}
		
		// marker stuff
		if (!entity.getMarker().getSymbol().isEmpty()) {
			domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
			domain.setMarkerSymbol(entity.getMarker().getSymbol());			
			domain.setChromosome(entity.getMarker().getChromosome());
			domain.setDetailClip(entity.getMarker().getDetailClipNote().get(0).getNote());		
		}
		
		// reference associations
		if (!entity.getRefAssocs().isEmpty()) {
			MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
			Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
			domain.getRefAssocs().sort(Comparator.comparing(MGIReferenceAssocDomain::getAllowOnlyOne).thenComparing(MGIReferenceAssocDomain::getRefAssocType));
		}	

		// synonyms
		if (entity.getSynonyms() != null && !entity.getSynonyms().isEmpty()) {
			Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getSynonyms());
			domain.setSynonyms(IteratorUtils.toList(i.iterator()));
			domain.getSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
		}
		
		// molecular mutations
		if (!entity.getMutations().isEmpty()) {
			AlleleMutationTranslator mutationTranslator = new AlleleMutationTranslator();
			Iterable<AlleleMutationDomain> i = mutationTranslator.translateEntities(entity.getMutations());
			domain.setMutations(IteratorUtils.toList(i.iterator()));
			domain.getMutations().sort(Comparator.comparing(AlleleMutationDomain::getMutation));
		}
		
		// subtype annotations
		if (!entity.getSubtypeAnnots().isEmpty()) {
			AnnotationTranslator annotTranslator = new AnnotationTranslator();
			Iterable<AnnotationDomain> i = annotTranslator.translateEntities(entity.getSubtypeAnnots());
			domain.setSubtypeAnnots(IteratorUtils.toList(i.iterator()));
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
		if (!entity.getDoAnnots().isEmpty()) {
			AnnotationTranslator annotTranslator = new AnnotationTranslator();
			Iterable<AnnotationDomain> i = annotTranslator.translateEntities(entity.getDoAnnots());
			domain.setDoAnnots(IteratorUtils.toList(i.iterator()));
		}
		
		return domain;
	}
	
}
