package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class SlimAlleleAnnotTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleAnnotDomain> {

	private SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();
	
	@Override
	protected SlimAlleleAnnotDomain entityToDomain(Allele entity) {
		
		SlimAlleleAnnotDomain domain = new SlimAlleleAnnotDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		
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
