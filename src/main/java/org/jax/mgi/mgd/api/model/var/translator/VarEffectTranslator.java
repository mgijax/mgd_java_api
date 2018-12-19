package org.jax.mgi.mgd.api.model.var.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.var.domain.VarEffectDomain;
import org.jax.mgi.mgd.api.model.var.entities.VarEffect;

public class VarEffectTranslator extends BaseEntityDomainTranslator<VarEffect, VarEffectDomain> {

	@Override
	protected VarEffectDomain entityToDomain(VarEffect entity, int translationDepth) {
		VarEffectDomain domain = new VarEffectDomain();
		domain.setVarEffectKey(String.valueOf(entity.get_varianteffect_key()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

	@Override
	protected VarEffect domainToEntity(VarEffectDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
