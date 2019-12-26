package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jboss.logging.Logger;

public class SlimAlleleTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimAlleleDomain entityToDomain(Allele entity) {
		
		SlimAlleleDomain domain = new SlimAlleleDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setAlleleStatus(entity.getAlleleStatus().getTerm());
		log.info("allele status: " + entity.getAlleleStatus().getTerm());
		
		// marker
		if (entity.getMarker() != null) {
			if (entity.getMarker().getLocationCache() != null) {
				domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
				domain.setMarkerSymbol(entity.getMarker().getSymbol());
				domain.setChromosome(entity.getMarker().getChromosome());
			}
		}
		
		// allele mgi id
		if (entity.getMgiAccessionIds() != null) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}
