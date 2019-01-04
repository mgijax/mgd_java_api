package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class SlimAlleleTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleVariantDomain> {

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	
	@Override
	protected SlimAlleleVariantDomain entityToDomain(Allele entity, int translationDepth) {
		
		SlimAlleleVariantDomain domain = new SlimAlleleVariantDomain();
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		//domain.setMgiAccessionId(entity.getMgiAccessionId().getAccID());

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
	protected Allele domainToEntity(SlimAlleleVariantDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
