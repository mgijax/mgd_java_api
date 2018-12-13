package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class SlimMarkerTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerDomain> {
	
	@Override
	protected SlimMarkerDomain entityToDomain(Marker entity, int translationDepth) {
			
		SlimMarkerDomain domain = new SlimMarkerDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
	
		return domain;
	}

	@Override
	protected Marker domainToEntity(SlimMarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
