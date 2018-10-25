package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.EventReasonDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.EventReason;

public class EventReasonTranslator extends BaseEntityDomainTranslator<EventReason, EventReasonDomain> {

	@Override
	protected EventReasonDomain entityToDomain(EventReason entity, int translationDepth) {
		EventReasonDomain domain = new EventReasonDomain();
		domain.setMarkerEventReasonKey(entity.get_marker_eventreason_key());
		domain.setEventReason(entity.getEventReason());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected EventReason domainToEntity(EventReasonDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
