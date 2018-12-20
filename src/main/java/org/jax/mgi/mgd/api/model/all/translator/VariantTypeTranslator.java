package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.VariantTypeDomain;
import org.jax.mgi.mgd.api.model.all.entities.VariantType;

public class VariantTypeTranslator extends BaseEntityDomainTranslator<VariantType, VariantTypeDomain> {

	@Override
	protected VariantTypeDomain entityToDomain(VariantType entity, int translationDepth) {
		VariantTypeDomain domain = new VariantTypeDomain();
		domain.setVariantTypeKey(String.valueOf(entity.get_varianttype_key()));
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
	protected VariantType domainToEntity(VariantTypeDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
