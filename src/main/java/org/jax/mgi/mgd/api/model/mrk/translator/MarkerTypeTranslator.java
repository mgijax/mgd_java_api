package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerType;

public class MarkerTypeTranslator extends BaseEntityDomainTranslator<MarkerType, MarkerTypeDomain> {

	@Override
	protected MarkerTypeDomain entityToDomain(MarkerType entity) {
		MarkerTypeDomain domain = new MarkerTypeDomain();
		domain.setMarkerTypeKey(String.valueOf(entity.get_marker_type_key()));
		domain.setMarkerType(entity.getName());
//		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
//		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
