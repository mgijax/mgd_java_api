package org.jax.mgi.mgd.api.model.var.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.var.domain.VarTypeDomain;
import org.jax.mgi.mgd.api.model.var.entities.VarType;

public class VarTypeTranslator extends BaseEntityDomainTranslator<VarType, VarTypeDomain> {

	@Override
	protected VarTypeDomain entityToDomain(VarType entity, int translationDepth) {
		VarTypeDomain domain = new VarTypeDomain();
		domain.setVarTypeKey(String.valueOf(entity.get_varianttype_key()));
		domain.setVariantKey(String.valueOf(entity.getVariant().get_variant_key()));			
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

	@Override
	protected VarType domainToEntity(VarTypeDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
