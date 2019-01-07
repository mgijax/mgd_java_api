package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class SlimAlleleTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleDomain> {
	
	@Override
	protected SlimAlleleDomain entityToDomain(Allele entity, int translationDepth) {
		
		SlimAlleleDomain domain = new SlimAlleleDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		
		return domain;
	}

	@Override
	protected Allele domainToEntity(SlimAlleleDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}