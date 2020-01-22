package org.jax.mgi.mgd.api.model.mrk.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GOTrackingDomain extends BaseDomain {
	
	private String processStatus;
	private String markerKey;
	private String completedByKey;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;	
	private String modifiedBy;
	private String completion_date;
	private String creation_date;
	private String modification_date;  
	private Integer isCompleted;
}
