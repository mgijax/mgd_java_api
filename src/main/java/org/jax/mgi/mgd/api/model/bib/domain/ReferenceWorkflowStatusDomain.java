package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceWorkflowStatusDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String refsKey;
	private String isCurrent;
	private String groupKey;
	private String group;
	private String groupAbbrev;
	private String statusKey;
	private String status;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 
}
