package org.jax.mgi.mgd.api.model.var.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeStrainTranslator;
import org.jax.mgi.mgd.api.model.var.domain.VarVariantDomain;
import org.jax.mgi.mgd.api.model.var.entities.VarVariant;

public class VarVariantTranslator extends BaseEntityDomainTranslator<VarVariant, VarVariantDomain> {
	
	private SlimAlleleTranslator alleleTranslator = new SlimAlleleTranslator();
	private SlimProbeStrainTranslator strainTranslator = new SlimProbeStrainTranslator();
	
	@Override
	protected VarVariantDomain entityToDomain(VarVariant entity, int translationDepth) {
		VarVariantDomain domain = new VarVariantDomain();
		domain.setVariantKey(String.valueOf(entity.get_variant_key()));
		domain.setIsReviewed(String.valueOf(entity.getIsReviewed()));
		domain.setDescription(entity.getDescription());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// using slim domains
		domain.setAllele(alleleTranslator.translate(entity.getAllele()));
		domain.setStrain(strainTranslator.translate(entity.getStrain()));
		
		if (entity.getSourceVariant() != null) {
			domain.setSourceVariantKey(String.valueOf(entity.getSourceVariant().get_variant_key()));
		}
		
		return domain;
	}

	@Override
	protected VarVariant domainToEntity(VarVariantDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
