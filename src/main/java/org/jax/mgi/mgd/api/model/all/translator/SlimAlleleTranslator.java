package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class SlimAlleleTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleDomain> {

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	
	@Override
	protected SlimAlleleDomain entityToDomain(Allele entity, int translationDepth) {
		
		SlimAlleleDomain domain = new SlimAlleleDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		return domain;
	}

	@Override
	protected Allele domainToEntity(SlimAlleleDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}