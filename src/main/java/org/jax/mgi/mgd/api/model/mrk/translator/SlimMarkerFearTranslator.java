package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerFearDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jboss.logging.Logger;

public class SlimMarkerFearTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerFearDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected SlimMarkerFearDomain entityToDomain(Marker entity) {
		
		SlimMarkerFearDomain domain = new SlimMarkerFearDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMarkerDisplay(entity.getSymbol() + ", " + entity.getName());		
		domain.setMarkerSymbol(entity.getSymbol());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}
