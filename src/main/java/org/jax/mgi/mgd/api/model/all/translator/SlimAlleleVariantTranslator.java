package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;

public class SlimAlleleVariantTranslator extends BaseEntityDomainTranslator<AlleleVariant, SlimAlleleVariantDomain> {
	
	@Override
	protected SlimAlleleVariantDomain entityToDomain(AlleleVariant entity, int translationDepth) {
		
		SlimAlleleVariantDomain domain = new SlimAlleleVariantDomain();
		
		domain.setVariantKey(String.valueOf(entity.get_variant_key()));
		domain.setAlleleKey(String.valueOf(entity.getAllele().get_allele_key()));
		domain.setSymbol(entity.getAllele().getSymbol());
	
		return domain;
	}

}
