package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeImageDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;

public class SlimGenotypeImageTranslator extends BaseEntityDomainTranslator<Genotype, SlimGenotypeImageDomain> {

	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected SlimGenotypeImageDomain entityToDomain(Genotype entity) {
		
		SlimGenotypeImageDomain domain = new SlimGenotypeImageDomain();
		
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
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
