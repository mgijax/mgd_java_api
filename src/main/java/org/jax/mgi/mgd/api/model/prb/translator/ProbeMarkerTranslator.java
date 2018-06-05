package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;

public class ProbeMarkerTranslator extends BaseEntityDomainTranslator<ProbeMarker, ProbeMarkerDomain> {

	@Override
	protected ProbeMarkerDomain entityToDomain(ProbeMarker entity, int translationDepth) {
		
		ProbeMarkerDomain domain = new ProbeMarkerDomain();
		domain.setKey(entity.getKey());
		domain.setRelationship(entity.getRelationship());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected ProbeMarker domainToEntity(ProbeMarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
