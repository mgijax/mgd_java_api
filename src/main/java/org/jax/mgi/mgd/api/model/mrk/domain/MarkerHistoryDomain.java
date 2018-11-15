package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerHistoryDomain extends BaseDomain {
	
	private int assocKey;
	private int markerKey;
	private int sequenceNum;
	private Integer markerEventKey;
	private String markerEvent;
	private Integer markerEventReasonKey;
	private String markerEventReason;
	private String markerHistorySymbolKey;
	private String markerHistorySymbol;
	private String markerHistoryName;
	private Integer refKey;
	private String jnumid;
	private String short_citation;
	private Date event_date;
	private Integer createdByKey;
	private String createdBy;
	private Integer modifiedByKey;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;    
}
