package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;

public class MarkerHistoryTranslator extends BaseEntityDomainTranslator<MarkerHistory, MarkerHistoryDomain> {

	@Override
	protected MarkerHistoryDomain entityToDomain(MarkerHistory entity, int translationDepth) {
		MarkerHistoryDomain domain = new MarkerHistoryDomain();
		
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		
		domain.setMarkerEventKey(String.valueOf(entity.getMarkerEvent().get_marker_event_key()));
		domain.setMarkerEvent(String.valueOf(entity.getMarkerEvent().getEvent()));
		domain.setMarkerEventReasonKey(String.valueOf(entity.getMarkerEventReason().get_marker_eventreason_key()));
		domain.setMarkerEventReason(entity.getMarkerEventReason().getEventReason());
		
		// int -> Integer -> String
		domain.setMarkerHistorySymbolKey(Integer.valueOf(entity.getMarkerHistory().get_marker_key()).toString());
		domain.setMarkerHistorySymbol(entity.getMarkerHistory().getSymbol());
		domain.setMarkerHistoryName(entity.getName());
		
		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
			domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		}
		
		if (entity.getEvent_date() != null) {
			domain.setEvent_date(dateFormatNoTime.format(entity.getEvent_date()));
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

	@Override
	protected MarkerHistory domainToEntity(MarkerHistoryDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
