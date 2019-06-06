package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;

public class SlimGenotypeTranslator extends BaseEntityDomainTranslator<Genotype, SlimGenotypeDomain> {

	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected SlimGenotypeDomain entityToDomain(Genotype entity) {
		
		SlimGenotypeDomain domain = new SlimGenotypeDomain();
		
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
		
		// see teleuse/EI/Genotype results
		// use combination of Genotype fields to produce the GenotypeDisplay
		//domain.setGenotypeDisplay(entity.getGenotypeDisplay());
		
		// mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}

		return domain;
	}

}
