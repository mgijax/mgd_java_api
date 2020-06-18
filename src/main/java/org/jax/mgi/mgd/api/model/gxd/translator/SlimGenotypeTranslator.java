package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;

public class SlimGenotypeTranslator extends BaseEntityDomainTranslator<Genotype, SlimGenotypeDomain> {
	
	@Override
	protected SlimGenotypeDomain entityToDomain(Genotype entity) {
		
		SlimGenotypeDomain domain = new SlimGenotypeDomain();
		
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
		
		// use combination of Genotype fields to produce the GenotypeDisplay
		// This is set in the Service search method.
		
		// mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		return domain;
	}

}
