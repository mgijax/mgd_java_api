package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class SlimAlleleAnnotTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleAnnotDomain> {
	
	@Override
	protected SlimAlleleAnnotDomain entityToDomain(Allele entity) {
		
		SlimAlleleAnnotDomain domain = new SlimAlleleAnnotDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		
		// mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		return domain;
	}

}
