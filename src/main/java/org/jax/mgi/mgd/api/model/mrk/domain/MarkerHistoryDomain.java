package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerHistoryDomain extends BaseDomain {
	
	private String processStatus;
	private String assocKey;
	private String markerKey;
	private String sequenceNum;
	private String markerEventKey;
	private String markerEvent;
	private String markerEventReasonKey;
	private String markerEventReason;
	private String markerHistorySymbolKey;
	private String markerHistorySymbol;
	private String markerHistoryName;
	private String refKey;
	private String jnumid;
	private String short_citation;
	private String event_date;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;    
}
