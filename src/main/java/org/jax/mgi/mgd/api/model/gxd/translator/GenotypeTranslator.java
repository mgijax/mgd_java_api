package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocViewDomain;
import org.jax.mgi.mgd.api.model.img.translator.ImagePaneAssocViewTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jboss.logging.Logger;

public class GenotypeTranslator extends BaseEntityDomainTranslator<Genotype, GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private NoteTranslator noteTranslator = new NoteTranslator();		
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private AllelePairTranslator allelePairsTranslator = new AllelePairTranslator();
	private ImagePaneAssocViewTranslator imagePaneTranslator = new ImagePaneAssocViewTranslator();
	
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
		domain.setUseAllelePairDefaultOrder("1");
		
		// at most one captionNote
		if (entity.getAlleleDetailNote() != null && !entity.getAlleleDetailNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getAlleleDetailNote());
			domain.setAlleleDetailNote(note.iterator().next());
		}

		// at most one copyrightNote
		if (entity.getGeneralNote() != null && !entity.getGeneralNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getGeneralNote());
			domain.setGeneralNote(note.iterator().next());
		}
		
		// at most one privateCuratorialNote
		if (entity.getPrivateCuratorialNote() != null && !entity.getPrivateCuratorialNote().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getPrivateCuratorialNote());
			domain.setPrivateCuratorialNote(note.iterator().next());
		}
				
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}

		// allele pairs
		if (entity.getAllelePairs() != null && !entity.getAllelePairs().isEmpty()) {
			Iterable<AllelePairDomain> t = allelePairsTranslator.translateEntities(entity.getAllelePairs());
			domain.setAllelePairs(IteratorUtils.toList(t.iterator()));
		}
		
		// imagepane associations by genotype
		if (entity.getImagePaneAssocs() != null && !entity.getImagePaneAssocs().isEmpty()) {
			Iterable<ImagePaneAssocViewDomain> t = imagePaneTranslator.translateEntities(entity.getImagePaneAssocs());
			domain.setImagePaneAssocs(IteratorUtils.toList(t.iterator()));
		}
					
		return domain;
	}

}
