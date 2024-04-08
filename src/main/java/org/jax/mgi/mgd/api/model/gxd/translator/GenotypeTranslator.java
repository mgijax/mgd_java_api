package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocViewDomain;
import org.jax.mgi.mgd.api.model.img.translator.ImagePaneAssocViewTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class GenotypeTranslator extends BaseEntityDomainTranslator<Genotype, GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private NoteTranslator noteTranslator = new NoteTranslator();		
	private AllelePairTranslator allelePairsTranslator = new AllelePairTranslator();
	private ImagePaneAssocViewTranslator imagePaneTranslator = new ImagePaneAssocViewTranslator();
	private AnnotationTranslator annotTranslator = new AnnotationTranslator();
	
	@Override
	protected GenotypeDomain entityToDomain(Genotype entity) {
		
		GenotypeDomain domain = new GenotypeDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
			
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setStrain(entity.getStrain().getStrain());
		domain.setIsConditional(String.valueOf(entity.getIsConditional()));
		domain.setExistsAsKey(String.valueOf(entity.getExistsAs().get_term_key()));
		domain.setExistsAs(entity.getExistsAs().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// yes, use default allele pair ordering
		domain.setEditAllelePairOrder(false);
		
		// at most one alleleDetailNote
		if (entity.getAlleleDetailNote() != null && !entity.getAlleleDetailNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getAlleleDetailNote());
			domain.setAlleleDetailNote(note.iterator().next());
		}

		// at most one generalNote
		if (entity.getGeneralNote() != null && !entity.getGeneralNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getGeneralNote());
			List<NoteDomain> noteDomain = (IteratorUtils.toList(note.iterator()));
			String allNotes = "";
			// merge all notes into the first note
			for (int i = 0; i < noteDomain.size(); i++) {
				allNotes += noteDomain.get(i).getNoteChunk() + " ";
			}
			domain.setGeneralNote(note.iterator().next());			
			domain.getGeneralNote().setNoteChunk(allNotes);			
		}
		
		// at most one privateCuratorialNote
		if (entity.getPrivateCuratorialNote() != null && !entity.getPrivateCuratorialNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getPrivateCuratorialNote());
			List<NoteDomain> noteDomain = (IteratorUtils.toList(note.iterator()));
			String allNotes = "";
			// merge all notes into the first note
			for (int i = 0; i < noteDomain.size(); i++) {
				allNotes += noteDomain.get(i).getNoteChunk() + " ";
			}
			domain.setPrivateCuratorialNote(note.iterator().next());			
			domain.getPrivateCuratorialNote().setNoteChunk(allNotes);	
		}
				
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// resource identifier ids only
		if (entity.getResourceIdentifierAccessionIds() != null && !entity.getResourceIdentifierAccessionIds().isEmpty()) {
			domain.setResourceIdentifierID(entity.getResourceIdentifierAccessionIds().get(0).getAccID());
		}
		
		// allele pairs
		if (entity.getAllelePairs() != null && !entity.getAllelePairs().isEmpty()) {
			Iterable<AllelePairDomain> t = allelePairsTranslator.translateEntities(entity.getAllelePairs());
			domain.setAllelePairs(IteratorUtils.toList(t.iterator()));
			domain.getAllelePairs().sort(Comparator.comparingInt(AllelePairDomain::getSequenceNum));
		}
		
		// imagepane associations by genotype
		if (entity.getImagePaneAssocs() != null && !entity.getImagePaneAssocs().isEmpty()) {
			Iterable<ImagePaneAssocViewDomain> t = imagePaneTranslator.translateEntities(entity.getImagePaneAssocs());
			domain.setImagePaneAssocs(IteratorUtils.toList(t.iterator()));
		}

		// do annotations by genotype
		List<AnnotationDomain> newDoList  = new ArrayList<AnnotationDomain>();
		if (entity.getDoAnnots() != null && !entity.getDoAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getDoAnnots());			
			newDoList.addAll(IteratorUtils.toList(t.iterator()));
		}
		domain.setDoAnnots(newDoList);
		domain.getDoAnnots().sort(Comparator.comparing(AnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER));
		
		// mp annotations by genotype
		List<AnnotationDomain> newMpList  = new ArrayList<AnnotationDomain>();		
		if (entity.getMpAnnots() != null && !entity.getMpAnnots().isEmpty()) {
			Iterable<AnnotationDomain> t = annotTranslator.translateEntities(entity.getMpAnnots());
			newMpList.addAll(IteratorUtils.toList(t.iterator()));		
		}
		domain.setMpAnnots(newMpList);
		domain.getMpAnnots().sort(Comparator.comparing(AnnotationDomain::getTerm, String.CASE_INSENSITIVE_ORDER));
		
		return domain;
	}

}
