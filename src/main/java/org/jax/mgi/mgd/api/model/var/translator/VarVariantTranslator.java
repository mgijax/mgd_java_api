package org.jax.mgi.mgd.api.model.var.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.var.domain.VarVariantDomain;
import org.jax.mgi.mgd.api.model.var.entities.VarVariant;

public class VarVariantTranslator extends BaseEntityDomainTranslator<VarVariant, VarVariantDomain> {
	
	@Override
	protected VarVariantDomain entityToDomain(VarVariant entity, int translationDepth) {
		VarVariantDomain domain = new VarVariantDomain();
		domain.setVariantKey(String.valueOf(entity.get_variant_key()));
		domain.setIsReviewed(String.valueOf(entity.getIsReviewed()));
		domain.setDescription(entity.getDescription());
		domain.setAlleleKey(String.valueOf(entity.getAllele().get_allele_key()));
		domain.setAlleleSymbol(entity.getAllele().getSymbol());
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setStrain(entity.getStrain().getStrain());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
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
