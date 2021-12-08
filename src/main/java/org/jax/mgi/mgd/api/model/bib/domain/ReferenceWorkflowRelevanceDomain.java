package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceWorkflowRelevanceDomain extends BaseDomain {
	
	private String processStatus;
	private String assocKey;
	private String refsKey;
	private Boolean isCurrent;
	private Double confidence;
	private String version;
	private String relevanceKey;
	private String relevanceTerm;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}
