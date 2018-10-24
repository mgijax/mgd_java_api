package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerType;

public class MarkerTypeTranslator extends BaseEntityDomainTranslator<MarkerType, MarkerTypeDomain> {

	@Override
	protected MarkerTypeDomain entityToDomain(MarkerType entity, int translationDepth) {
		MarkerTypeDomain domain = new MarkerTypeDomain();
		domain.setMarkerTypeKey(entity.get_marker_type_key());
		domain.setMarkerType(entity.getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected MarkerType domainToEntity(MarkerTypeDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
