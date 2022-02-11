package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFEARDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jboss.logging.Logger;

public class SlimAlleleFEARTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleFEARDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected SlimAlleleFEARDomain entityToDomain(Allele entity) {
		
		SlimAlleleFEARDomain domain = new SlimAlleleFEARDomain();

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
