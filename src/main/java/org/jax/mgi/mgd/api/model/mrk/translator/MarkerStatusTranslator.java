package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerStatusDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerStatus;

public class MarkerStatusTranslator extends BaseEntityDomainTranslator<MarkerStatus, MarkerStatusDomain> {

	@Override
	protected MarkerStatusDomain entityToDomain(MarkerStatus entity, int translationDepth) {
		MarkerStatusDomain domain = new MarkerStatusDomain();
		domain.setMarkerStatusKey(entity.get_marker_status_key());
		domain.setMarkerStatus(entity.getStatus());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected MarkerStatus domainToEntity(MarkerStatusDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
