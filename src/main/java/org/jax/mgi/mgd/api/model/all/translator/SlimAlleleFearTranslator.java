package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFearDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jboss.logging.Logger;

public class SlimAlleleFearTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected SlimAlleleFearDomain entityToDomain(Allele entity) {
		
		SlimAlleleFearDomain domain = new SlimAlleleFearDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}
