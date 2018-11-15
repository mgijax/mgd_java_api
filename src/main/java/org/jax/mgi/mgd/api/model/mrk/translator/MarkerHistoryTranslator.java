package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;

public class MarkerHistoryTranslator extends BaseEntityDomainTranslator<MarkerHistory, MarkerHistoryDomain> {

	@Override
	protected MarkerHistoryDomain entityToDomain(MarkerHistory entity, int translationDepth) {
		MarkerHistoryDomain domain = new MarkerHistoryDomain();
		
		domain.setAssocKey(entity.get_assoc_key());
		domain.setMarkerKey(entity.get_marker_key());
		domain.setSequenceNum(entity.getSequenceNum());
		
		domain.setMarkerEventKey(entity.getMarkerEvent().get_marker_event_key());
		domain.setMarkerEvent(entity.getMarkerEvent().getEvent());
		domain.setMarkerEventReasonKey(entity.getMarkerEventReason().get_marker_eventreason_key());
		domain.setMarkerEventReason(entity.getMarkerEventReason().getEventReason());
		
		// int -> Integer -> String
		domain.setMarkerHistorySymbolKey(Integer.valueOf(entity.getMarkerHistory().get_marker_key()).toString());
		domain.setMarkerHistorySymbol(entity.getMarkerHistory().getSymbol());
		domain.setMarkerHistoryName(entity.getName());
		
		if (entity.getReference() != null) {
			domain.setRefKey(entity.getReference().get_refs_key());
			domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
			domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setEvent_date(entity.getEvent_date());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		return domain;
	}

	@Override
	protected MarkerHistory domainToEntity(MarkerHistoryDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
