package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerHistoryDomain extends BaseDomain {
	
	private Integer markerKey;
	private Integer sequenceNum;
	private Integer markerEventKey;
	private String markerEvent;
	private Integer markerEventReasonKey;
	private String markerEventReason;
	private Integer markerHistoryKey;
	private String markerHistorySymbol;
	private String markerHistoryName;
	private Integer refKey;
	private String jnumid;
	private Date event_date;
	private Integer createdByKey;
	private String createdBy;
	private Integer modifiedByKey;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;    
}
