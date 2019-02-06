package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantAnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AlleleVariantAnnotationTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AlleleVariantTranslator extends BaseEntityDomainTranslator<AlleleVariant, AlleleVariantDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private SlimAlleleTranslator alleleTranslator = new SlimAlleleTranslator();
	private SlimProbeStrainTranslator strainTranslator = new SlimProbeStrainTranslator();
	private AlleleVariantAnnotationTranslator variantAnnotationTranslator = new AlleleVariantAnnotationTranslator();
	private VariantSequenceTranslator variantSequenceTranslator = new VariantSequenceTranslator();
	private NoteTranslator noteTranslator = new NoteTranslator();
	private MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
	
	@Override
	protected AlleleVariantDomain entityToDomain(AlleleVariant entity, int translationDepth) {
		
		AlleleVariantDomain domain = new AlleleVariantDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setVariantKey(String.valueOf(entity.get_variant_key()));
		domain.setIsReviewed(String.valueOf(entity.getIsReviewed()));
		domain.setDescription(entity.getDescription());
		domain.setChromosome(entity.getAllele().getMarker().getChromosome());
		domain.setStrand(entity.getAllele().getMarker().getLocationCache().getStrand());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		domain.setAllele(alleleTranslator.translate(entity.getAllele()));
		domain.setStrain(strainTranslator.translate(entity.getStrain()));

		// a curated variant has a source variant
		// a source variant does *not* have a source variant (null)
		if (entity.getSourceVariant() != null) {
			AlleleVariantTranslator sourceVariantTranslator = new AlleleVariantTranslator();		
			domain.setSourceVariant(sourceVariantTranslator.translate(entity.getSourceVariant()));
		}
			
        if (entity.getVariantTypes() != null) {
        	Iterable<AlleleVariantAnnotationDomain> i = variantAnnotationTranslator.translateEntities(entity.getVariantTypes());
        	if(i.iterator().hasNext() == true) {
                domain.setVariantTypes(IteratorUtils.toList(i.iterator()));
        	}
        }
        
        if (entity.getVariantEffects() != null) {
            Iterable<AlleleVariantAnnotationDomain> i = variantAnnotationTranslator.translateEntities(entity.getVariantEffects());
          	if(i.iterator().hasNext() == true) {
                domain.setVariantEffects(IteratorUtils.toList(i.iterator()));
            }
        }

        if (entity.getVariantSequences() != null) {
            Iterable<VariantSequenceDomain> i = variantSequenceTranslator.translateEntities(entity.getVariantSequences());
          	if(i.iterator().hasNext() == true) {
                domain.setVariantSequences(IteratorUtils.toList(i.iterator()));
            }
        }

		// at most one curatorNote; list of 1
		if (entity.getCuratorNote() != null) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getCuratorNote());
			if(note.iterator().hasNext() == true) {
				domain.setCuratorNote(note.iterator().next());
			}
		}

		// at most one publicNote; list of 1
		if (entity.getPublicNote() != null) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getPublicNote());
			if(note.iterator().hasNext() == true) {
				domain.setPublicNote(note.iterator().next());
			}
		}
		
		// reference associations
		if (entity.getRefAssocs() != null) {
			Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			if(i.iterator().hasNext() == true) {
				domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
			}
		}
		
		return domain;
	}

	@Override
	protected AlleleVariant domainToEntity(AlleleVariantDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
