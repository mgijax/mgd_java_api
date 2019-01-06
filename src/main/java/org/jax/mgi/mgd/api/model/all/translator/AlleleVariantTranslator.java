package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantEffectDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AlleleVariantTypeDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AlleleVariantEffectTranslator;
import org.jax.mgi.mgd.api.model.voc.translator.AlleleVariantTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;

public class AlleleVariantTranslator extends BaseEntityDomainTranslator<AlleleVariant, AlleleVariantDomain> {
	
	private SlimAlleleTranslator alleleTranslator = new SlimAlleleTranslator();
	private SlimProbeStrainTranslator strainTranslator = new SlimProbeStrainTranslator();
	private AlleleVariantTypeTranslator variantTypeTranslator = new AlleleVariantTypeTranslator();
	private AlleleVariantEffectTranslator variantEffectTranslator = new AlleleVariantEffectTranslator();
	@Override
	protected AlleleVariantDomain entityToDomain(AlleleVariant entity, int translationDepth) {
		AlleleVariantDomain domain = new AlleleVariantDomain();
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
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		// using slim domains
		domain.setAllele(alleleTranslator.translate(entity.getAllele()));
		domain.setStrain(strainTranslator.translate(entity.getStrain()));
		
		if (entity.getSourceVariant() != null) {
			domain.setSourceVariantKey(String.valueOf(entity.getSourceVariant().get_variant_key()));
		}
		
        if (entity.getVariantTypes() != null) {
        Iterable<AlleleVariantTypeDomain> i = variantTypeTranslator.translateEntities(entity.getVariantTypes());
        	if(i.iterator().hasNext() == true) {
                domain.setTypes(IteratorUtils.toList(i.iterator()));
        	}
        }
        if (entity.getVariantEffects() != null) {
            Iterable<AlleleVariantEffectDomain> i = variantEffectTranslator.translateEntities(entity.getVariantEffects());
            	if(i.iterator().hasNext() == true) {
                    domain.setEffects(IteratorUtils.toList(i.iterator()));
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
