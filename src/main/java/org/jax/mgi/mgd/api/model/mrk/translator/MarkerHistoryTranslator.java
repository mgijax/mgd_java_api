package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistory;
import org.jax.mgi.mgd.api.util.Constants;

public class MarkerHistoryTranslator extends BaseEntityDomainTranslator<MarkerHistory, MarkerHistoryDomain> {

	@Override
	protected MarkerHistoryDomain entityToDomain(MarkerHistory entity) {
		MarkerHistoryDomain domain = new MarkerHistoryDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		
		domain.setMarkerEventKey(String.valueOf(entity.getMarkerEvent().get_term_key()));
		domain.setMarkerEvent(String.valueOf(entity.getMarkerEvent().getTerm()));
		domain.setMarkerEventReasonKey(String.valueOf(entity.getMarkerEventReason().get_term_key()));
		domain.setMarkerEventReason(entity.getMarkerEventReason().getTerm());
		
		// int -> Integer -> String
		domain.setMarkerHistorySymbolKey(Integer.valueOf(entity.getMarkerHistory().get_marker_key()).toString());
		domain.setMarkerHistorySymbol(entity.getMarkerHistory().getSymbol());
		domain.setMarkerHistoryName(entity.getName());
		
		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getJnumid());
			domain.setShort_citation(entity.getReference().getShort_citation());
		}
		
		if (entity.getEvent_date() != null) {
			domain.setEvent_date(dateFormatNoTime.format(entity.getEvent_date()));
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
