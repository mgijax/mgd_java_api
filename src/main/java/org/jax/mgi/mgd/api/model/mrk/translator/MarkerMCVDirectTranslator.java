package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerMCVDirectDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerMCVCache;

public class MarkerMCVDirectTranslator extends BaseEntityDomainTranslator<MarkerMCVCache, MarkerMCVDirectDomain> {

	@Override
	protected MarkerMCVDirectDomain entityToDomain(MarkerMCVCache entity) {
		MarkerMCVDirectDomain domain = new MarkerMCVDirectDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMcvTermKey(String.valueOf(entity.get_MCVTerm_key()));
		domain.setTerm(entity.getTerm());
		domain.setQualifier(entity.getQualifier());
		domain.setDirectTerms(entity.getDirectTerms());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
