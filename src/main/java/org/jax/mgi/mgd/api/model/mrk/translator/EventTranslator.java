package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.EventDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Event;

public class EventTranslator extends BaseEntityDomainTranslator<Event, EventDomain> {

	@Override
	protected EventDomain entityToDomain(Event entity, int translationDepth) {
		EventDomain domain = new EventDomain();
		domain.setMarkerEventKey(String.valueOf(entity.get_marker_event_key()));
		domain.setEvent(entity.getEvent());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
