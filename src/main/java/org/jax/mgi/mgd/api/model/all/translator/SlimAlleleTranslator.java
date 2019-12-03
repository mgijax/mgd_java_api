package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;

public class SlimAlleleTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleDomain> {

	
	@Override
	protected SlimAlleleDomain entityToDomain(Allele entity) {
		
		SlimAlleleDomain domain = new SlimAlleleDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		
		// marker
		if (entity.getMarker() != null) {
			if (entity.getMarker().getLocationCache() != null) {
				domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
				domain.setMarkerSymbol(entity.getMarker().getSymbol());
			}
		}
		
		// allele mgi id
		if (entity.getMgiAccessionIds() != null) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}
