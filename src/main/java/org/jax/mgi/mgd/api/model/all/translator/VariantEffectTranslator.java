package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.VariantEffectDomain;
import org.jax.mgi.mgd.api.model.all.entities.VariantEffect;

public class VariantEffectTranslator extends BaseEntityDomainTranslator<VariantEffect, VariantEffectDomain> {

	@Override
	protected VariantEffectDomain entityToDomain(VariantEffect entity, int translationDepth) {
		VariantEffectDomain domain = new VariantEffectDomain();
		domain.setVariantEffectKey(String.valueOf(entity.get_varianteffect_key()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

	@Override
	protected VariantEffect domainToEntity(VariantEffectDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
