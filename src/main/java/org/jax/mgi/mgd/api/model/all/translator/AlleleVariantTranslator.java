package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;

public class AlleleVariantTranslator extends BaseEntityDomainTranslator<AlleleVariant, AlleleVariantDomain> {
	
	private SlimAlleleTranslator alleleTranslator = new SlimAlleleTranslator();
	private SlimProbeStrainTranslator strainTranslator = new SlimProbeStrainTranslator();
	
	@Override
	protected AlleleVariantDomain entityToDomain(AlleleVariant entity, int translationDepth) {
		AlleleVariantDomain domain = new AlleleVariantDomain();
		System.out.println("Translating variant key");
		domain.setVariantKey(String.valueOf(entity.get_variant_key()));
		System.out.println("Translating isReviewed");
		domain.setIsReviewed(String.valueOf(entity.getIsReviewed()));
		System.out.println("Translating description");
		domain.setDescription(entity.getDescription());
		System.out.println("Translating createdByKey");
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		System.out.println("Translating createdBy");
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		System.out.println("Translating modifiedByKey");
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		System.out.println("Translating modifiedBy");
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		System.out.println("Translating creationDate");
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		System.out.println("Translating modificationDate");
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// using slim domains
		System.out.println("Allele");
		domain.setAllele(alleleTranslator.translate(entity.getAllele()));
		System.out.println("Translating Strain");
		domain.setStrain(strainTranslator.translate(entity.getStrain()));
		 System.out.println("Translating checking if sourceVariant is null");
		if (entity.getSourceVariant() != null) {
			System.out.println("Translating sourceVariantKey");
			domain.setSourceVariantKey(String.valueOf(entity.getSourceVariant().get_variant_key()));
			
		}
		System.out.println("Translator returning domain");
		return domain;
	}

	@Override
	protected AlleleVariant domainToEntity(AlleleVariantDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
