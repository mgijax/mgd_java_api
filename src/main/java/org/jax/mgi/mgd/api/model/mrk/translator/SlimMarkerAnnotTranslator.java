package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class SlimMarkerAnnotTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerAnnotDomain> {
	
	@Override
	protected SlimMarkerAnnotDomain entityToDomain(Marker entity) {
		
		SlimMarkerAnnotDomain domain = new SlimMarkerAnnotDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMarkerDisplay(entity.getSymbol() + ", " + entity.getName());
		
		// mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		return domain;
	}

}
