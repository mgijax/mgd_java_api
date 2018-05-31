package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class AlleleTranslator extends BaseEntityDomainTranslator<Allele, AlleleDomain> {

	@Override
	protected AlleleDomain entityToDomain(Allele entity, int translationDepth) {
		
		AlleleDomain domain = new AlleleDomain();
		domain.set_allele_key(entity.get_allele_key());

		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected Allele domainToEntity(AlleleDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
